package de.vanguard.rebalancingservice.service

import de.vanguard.rebalancingservice.service.ReBalancingService.Companion.CASH_STRATEGY_ID
import de.vanguard.rebalancingservice.service.client.FPSClient
import de.vanguard.rebalancingservice.service.client.FPSProperties
import de.vanguard.rebalancingservice.service.exceptions.CustomerPortfolioNotFoundException
import de.vanguard.rebalancingservice.service.persistence.CustomerEntity
import de.vanguard.rebalancingservice.service.persistence.CustomerRepository
import de.vanguard.rebalancingservice.service.persistence.StrategyEntity
import de.vanguard.rebalancingservice.service.persistence.StrategyRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*
import org.mockito.Mockito.`when` as When

@ExtendWith(MockitoExtension::class)
internal class ReBalancingServiceTest(
    @Mock private val customerRepository: CustomerRepository,
    @Mock private val strategyRepository: StrategyRepository,
    @Mock private val fpsClient: FPSClient,
    @Mock private val fpsProperties: FPSProperties
) {

    @InjectMocks
    private lateinit var sut: ReBalancingService

    @Test
    internal fun `should load customers and strategies data`() {
        sut.loadDailyCustomersAndStrategies()
        verify(customerRepository, Mockito.times(1)).saveAll(Mockito.anyCollection())
        verify(strategyRepository, Mockito.times(1)).saveAll(Mockito.anyCollection())
    }


    @Test
    internal fun `should return empty list when no customers are found `() {
        assert(sut.getAllCustomers().isEmpty())
    }

    @Test
    internal fun `should return empty list when no strategies are found `() {
        assert(sut.getAllStrategies().isEmpty())
    }


    @Test
    internal fun `should return customers after loading the customers csv file `() {

        When(customerRepository.findAll()).thenReturn(
            listOf(
                CustomerEntity(
                    id = UUID.randomUUID(),
                    customerId = "1",
                    email = "ahsanabid@gmail.com",
                    riskLevel = 1,
                    retirementAge = 65,
                    dateOfBirth = LocalDate.now().minusYears(30),
                    createdAt = ZonedDateTime.now()
                )
            )
        )

        assert(sut.getAllCustomers().isNotEmpty())
    }

    @Test
    internal fun `should return strategies after loading the strategies csv file `() {
        When(strategyRepository.findAll()).thenReturn(
            listOf(
                StrategyEntity(
                    id = UUID.randomUUID(),
                    strategyId = "1",
                    minRiskLevel = 1,
                    maxRiskLevel = 10,
                    minYearsToRetirement = 10,
                    maxYearsToRetirement = 20,
                    stocksPercentage = BigDecimal("10"),
                    cashPercentage = BigDecimal("30"),
                    bondsPercentage = BigDecimal("50"),
                    createdAt = ZonedDateTime.now()
                )
            )
        )



        assert(sut.getAllStrategies().isNotEmpty())
    }


    @Test
    internal fun `should get a matching strategy for a customer `() {
        val customerId = "1"
        When(customerRepository.findByCustomerId(customerId)).thenReturn(
            Optional.of(
                CustomerEntity(
                    id = UUID.randomUUID(),
                    customerId = "1",
                    email = "ahsanabid@gmail.com",
                    riskLevel = 1,
                    retirementAge = 65,
                    dateOfBirth = LocalDate.now().minusYears(30),
                    createdAt = ZonedDateTime.now()
                )
            )
        )


        When(strategyRepository.findAll()).thenReturn(
            listOf(
                StrategyEntity(
                    id = UUID.randomUUID(),
                    strategyId = "1",
                    minRiskLevel = 1,
                    maxRiskLevel = 10,
                    minYearsToRetirement = 25,
                    maxYearsToRetirement = 40,
                    stocksPercentage = BigDecimal("20"),
                    cashPercentage = BigDecimal("30"),
                    bondsPercentage = BigDecimal("50"),
                    createdAt = ZonedDateTime.now()
                ), StrategyEntity(
                    id = UUID.randomUUID(),
                    strategyId = "2",
                    minRiskLevel = 1,
                    maxRiskLevel = 10,
                    minYearsToRetirement = 25,
                    maxYearsToRetirement = 40,
                    stocksPercentage = BigDecimal("20"),
                    cashPercentage = BigDecimal("20"),
                    bondsPercentage = BigDecimal("60"),
                    createdAt = ZonedDateTime.now()
                )
            )
        )

        Assertions.assertThat(sut.getStrategiesSuitableForCustomer(customerId = "1")).isNotEmpty
    }


    @Test
    internal fun `should provide cash strategy when no matching strategy found for a customer with risk and retirement years`() {
        val customerId = "1"
        When(customerRepository.findByCustomerId(customerId)).thenReturn(
            Optional.of(
                CustomerEntity(
                    id = UUID.randomUUID(),
                    customerId = "1",
                    email = "ahsanabid@gmail.com",
                    riskLevel = 1,
                    retirementAge = 65,
                    dateOfBirth = LocalDate.now().minusYears(30),
                    createdAt = ZonedDateTime.now()
                )
            )
        )


        When(strategyRepository.findAll()).thenReturn(
            listOf(
                StrategyEntity(
                    id = UUID.randomUUID(),
                    strategyId = "1",
                    minRiskLevel = 1,
                    maxRiskLevel = 10,
                    minYearsToRetirement = 25,
                    maxYearsToRetirement = 34,
                    stocksPercentage = BigDecimal("10"),
                    cashPercentage = BigDecimal("30"),
                    bondsPercentage = BigDecimal("50"),
                    createdAt = ZonedDateTime.now()
                )
            )
        )



        When(strategyRepository.findByStrategyId(strategyId = CASH_STRATEGY_ID)).thenReturn(
            Optional.of(
                StrategyEntity(
                    id = UUID.randomUUID(),
                    strategyId = "CASH_ID",
                    minRiskLevel = 0,
                    maxRiskLevel = 0,
                    minYearsToRetirement = 0,
                    maxYearsToRetirement = 0,
                    stocksPercentage = BigDecimal("0"),
                    cashPercentage = BigDecimal("100"),
                    bondsPercentage = BigDecimal("0"),
                    createdAt = ZonedDateTime.now()
                )
            )
        )

        val strategiesSuitableForCustomer = sut.getStrategiesSuitableForCustomer(customerId = customerId)
        assert(strategiesSuitableForCustomer.isNotEmpty())
        assert(strategiesSuitableForCustomer.first().strategyId == CASH_STRATEGY_ID)

    }


    @Test
    internal fun `should throw CustomerPortfolioNotFoundException when no matching portfolio found for a customer `() {
        val customerId = "1"

        assertThrows<CustomerPortfolioNotFoundException> {
            sut.getPortfolioForCustomer(customerId = customerId)
        }
        verify(fpsClient, Mockito.times(1)).getCustomerInfo(customerId = customerId)
    }


    @Test
    internal fun `should update a valid strategy for a customer `() {
        val customerId = "1"
        val strategyId = "1"

        When(customerRepository.findByCustomerId(customerId)).thenReturn(
            Optional.of(
                CustomerEntity(
                    id = UUID.randomUUID(),
                    customerId = "1",
                    email = "ahsanabid@gmail.com",
                    riskLevel = 1,
                    retirementAge = 65,
                    dateOfBirth = LocalDate.now().minusYears(30),
                    createdAt = ZonedDateTime.now()
                )
            )
        )

        When(strategyRepository.findByStrategyId(strategyId = strategyId)).thenReturn(
            Optional.of(
                StrategyEntity(
                    id = UUID.randomUUID(),
                    strategyId = "1",
                    minRiskLevel = 1,
                    maxRiskLevel = 10,
                    minYearsToRetirement = 25,
                    maxYearsToRetirement = 36,
                    stocksPercentage = BigDecimal("10"),
                    cashPercentage = BigDecimal("30"),
                    bondsPercentage = BigDecimal("50"),
                    createdAt = ZonedDateTime.now()
                )
            )
        )

        assertDoesNotThrow {
            sut.updateStrategyForCustomer(customerId = customerId, strategyId = strategyId)
            verify(customerRepository, Mockito.times(1)).save(Mockito.any())
        }

    }
}