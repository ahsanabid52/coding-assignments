package de.vanguard.rebalancingservice.service

import de.vanguard.rebalancingservice.service.client.*
import de.vanguard.rebalancingservice.service.exceptions.*
import de.vanguard.rebalancingservice.service.persistence.CustomerEntity
import de.vanguard.rebalancingservice.service.persistence.CustomerRepository
import de.vanguard.rebalancingservice.service.persistence.StrategyEntity
import de.vanguard.rebalancingservice.service.persistence.StrategyRepository
import mu.KotlinLogging
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils
import java.io.IOException
import java.io.InputStreamReader
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.transaction.Transactional


@Service
class ReBalancingService(
    private val customerRepository: CustomerRepository,
    private val strategyRepository: StrategyRepository,
    private val fpsClient: FPSClient,
    private val fpsProperties: FPSProperties
) {
    private val logger = KotlinLogging.logger {}

    companion object {
        const val CASH_STRATEGY_ID = "CASH_ID"
        val HUNDRED = BigDecimal("100")
    }

    @Transactional
    fun loadDailyCustomersAndStrategies() {
        logger.info { "Going to load the customers and strategies data into the system." }
        customerRepository.saveAll(parseAndLoadCustomersData("customers.csv"))
        strategyRepository.saveAll(
            parseAndLoadStrategiesData("strategies.csv").plus(
                StrategyEntity(
                    strategyId = CASH_STRATEGY_ID,
                    minRiskLevel = 0,
                    maxRiskLevel = 0,
                    minYearsToRetirement = 0,
                    maxYearsToRetirement = 0,
                    stocksPercentage = BigDecimal.ZERO,
                    cashPercentage = HUNDRED,
                    bondsPercentage = BigDecimal.ZERO,
                    createdAt = ZonedDateTime.now()
                )
            )
        )
    }

    private fun parseAndLoadStrategiesData(fileName: String): List<StrategyEntity> {
        val strategies = mutableListOf<StrategyEntity>()
        try {

            CSVParser(
                InputStreamReader(ResourceUtils.getURL(fileName).openStream()),
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(',')
            ).use { parser ->
                for (csvRecord in parser) {
                    strategies.add(
                        StrategyEntity(
                            strategyId = csvRecord[0],
                            minRiskLevel = csvRecord[1].toInt(),
                            maxRiskLevel = csvRecord[2].toInt(),
                            minYearsToRetirement = csvRecord[3].toInt(),
                            maxYearsToRetirement = csvRecord[4].toInt(),
                            stocksPercentage = csvRecord[5].toBigDecimal(),
                            cashPercentage = csvRecord[6].toBigDecimal(),
                            bondsPercentage = csvRecord[7].toBigDecimal(),
                            createdAt = ZonedDateTime.now()
                        )
                    )
                }
            }
        } catch (e: IOException) {
            logger.error { " Error while parsing and loading strategies data ${e.printStackTrace()}" }
            throw CSVParserFailedException()
        }
        return strategies
    }


    fun getStrategiesSuitableForCustomer(customerId: String): List<StrategyDto> {
        with(getCustomer(customerId)) {
            val yearsTillRetirement = calculateYearsTillRetirement(this)

            var strategies = strategyRepository.findAll()
                .filter { strategyEntity -> checkIfStrategyMatchesTheCustomer(strategyEntity, this, yearsTillRetirement) }
            if (strategies.isEmpty()) {
                logger.info { "No Suitable Strategy Found For Customer using the 100% cash option" }
                strategies = listOf(strategyRepository.findByStrategyId(strategyId = CASH_STRATEGY_ID).get())
            }

            logger.info { "Strategies matched to the customer is $strategies" }

            return strategies.map { strategyEntity -> strategyEntity.toDto() }.toList()
        }
    }

    fun getPortfolioForCustomer(customerId: String): CustomerPortfolioDto {
        return fpsClient.getCustomerInfo(customerId = customerId).orElseThrow { CustomerPortfolioNotFoundException() }
    }

    fun updateStrategyForCustomer(customerId: String, strategyId: String) {
        with(getCustomer(customerId)) {
            val yearsTillRetirement = calculateYearsTillRetirement(this)
            strategyRepository.findByStrategyId(strategyId = strategyId).orElseThrow { StrategyNotFoundException() }.also { strategyEntity ->
                if (strategyId == CASH_STRATEGY_ID || checkIfStrategyMatchesTheCustomer(strategyEntity, this, yearsTillRetirement)) {
                    this.selectedStrategy = strategyEntity
                    customerRepository.save(this)
                    logger.info { "Customer strategy updated" }
                } else {
                    throw InvalidStrategyProvidedForTheCustomer()
                }
            }
        }
    }


    fun getTradesForRebalancingCustomerPortfolio(customerId: String): CustomerTradeDto {
        val customerEntity = getCustomer(customerId)
        if (null == customerEntity.selectedStrategy) {
            throw NoStrategySelectedForCustomerException()
        }

        val portfolioForCustomer = getPortfolioForCustomer(customerId)
        val portfolioStats = getCurrentPortfolioStatsForTheCustomer(portfolioForCustomer)

        return CustomerTradeDto(
            customerId = customerId,
            stocks = calculateTradeForAssets(
                customerEntity.selectedStrategy!!.stocksPercentage,
                portfolioStats.stocksPercentage,
                portfolioStats.totalAssets
            ),
            bonds = calculateTradeForAssets(
                customerEntity.selectedStrategy!!.bondsPercentage,
                portfolioStats.bondsPercentage,
                portfolioStats.totalAssets
            ),
            cash = calculateTradeForAssets(
                customerEntity.selectedStrategy!!.cashPercentage,
                portfolioStats.cashPercentage,
                portfolioStats.totalAssets
            )
        )
    }


    fun executeTradesForRebalancingCustomerPortfolio(customersRequest: CustomersRequest) {

        if (customersRequest.customers.size >= fpsProperties.batchSize.toInt()) {
            logger.error { "Batch size limit exceeded ${fpsProperties.batchSize.toInt()} " }
            throw BatchSizeLimitForCustomerTradesExceededException()
        }

        val customerTrades = customersRequest.customers.distinct().map { customerId ->
            getTradesForRebalancingCustomerPortfolio(customerId = customerId)
        }.toList()


        if (fpsClient.executeTradesBatch(customerTrades = customerTrades).statusCode != HttpStatus.CREATED) {
            throw UnableToExecuteCustomerTradesException()
        }

    }


    fun getAllCustomers(): List<CustomerDto> {
        return customerRepository.findAll().map { customerEntity -> customerEntity.toDto() }.toList()
    }

    fun getAllStrategies(): List<StrategyDto> {
        return strategyRepository.findAll().map { strategyEntity -> strategyEntity.toDto() }.toList()
    }

    private fun parseAndLoadCustomersData(fileName: String): List<CustomerEntity> {
        val customers = mutableListOf<CustomerEntity>()
        try {
            CSVParser(
                InputStreamReader(ResourceUtils.getURL(fileName).openStream()),
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(',')
            ).use { parser ->
                for (csvRecord in parser) {
                    customers.add(
                        CustomerEntity(
                            customerId = csvRecord[0],
                            email = csvRecord[1],
                            dateOfBirth = LocalDate.parse(csvRecord[2], DateTimeFormatter.ISO_DATE),
                            riskLevel = csvRecord[3].toInt(),
                            retirementAge = csvRecord[4].toInt(),
                            createdAt = ZonedDateTime.now()
                        )
                    )
                }
            }
        } catch (e: IOException) {
            logger.error { "Error while parsing and loading customers data ${e.printStackTrace()}" }
            throw CSVParserFailedException()
        }
        return customers
    }

    private fun calculateTradeForAssets(customerPercentage: BigDecimal, fpsPercentage: BigDecimal, totalAssets: BigDecimal): BigDecimal {
        return ((customerPercentage.subtract(fpsPercentage)).divide(HUNDRED, 2, RoundingMode.HALF_UP)).multiply(totalAssets)

    }


    private fun getCurrentPortfolioStatsForTheCustomer(portfolioForCustomer: CustomerPortfolioDto): CustomerPortfolioStats {

        with(portfolioForCustomer) {
            val totalAssets = bonds.add(cash).add(stocks)

            return CustomerPortfolioStats(
                customerId = customerId,
                totalAssets = totalAssets,
                bondsPercentage = (bonds.divide(totalAssets, 2, RoundingMode.HALF_UP)).multiply(HUNDRED),
                cashPercentage = (cash.divide(totalAssets, 2, RoundingMode.HALF_UP)).multiply(HUNDRED),
                stocksPercentage = (stocks.divide(totalAssets, 2, RoundingMode.HALF_UP)).multiply(HUNDRED)
            )
        }
    }


    private fun checkIfStrategyMatchesTheCustomer(strategyEntity: StrategyEntity, customerEntity: CustomerEntity, yearsTillRetirement: Int): Boolean {
        with(customerEntity) {
            return riskLevel >= strategyEntity.minRiskLevel && riskLevel <= strategyEntity.maxRiskLevel
                    && yearsTillRetirement >= strategyEntity.minYearsToRetirement && yearsTillRetirement <= strategyEntity.maxYearsToRetirement

        }
    }

    private fun calculateYearsTillRetirement(customerEntity: CustomerEntity): Int {
        return customerEntity.retirementAge - (LocalDate.now().year - customerEntity.dateOfBirth.year)
    }

    private fun getCustomer(customerId: String): CustomerEntity {
        return customerRepository.findByCustomerId(customerId = customerId).orElseThrow { CustomerNotFoundException() }
    }


}