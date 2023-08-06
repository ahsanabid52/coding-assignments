package de.vanguard.rebalancingservice.service

import de.vanguard.rebalancingservice.service.client.CustomerPortfolioDto
import de.vanguard.rebalancingservice.service.client.CustomerTradeDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ReBalancingController(private val reBalancingService: ReBalancingService) : ReBalancingResource {
    override fun loadDailyCustomersAndStrategies() {
        reBalancingService.loadDailyCustomersAndStrategies()
    }

    override fun getAllCustomers(): List<CustomerDto> {
        return reBalancingService.getAllCustomers()
    }

    override fun getAllStrategies(): List<StrategyDto> {
        return reBalancingService.getAllStrategies()
    }

    override fun getStrategiesSuitableForCustomer(customerId: String): List<StrategyDto> {
        return reBalancingService.getStrategiesSuitableForCustomer(customerId)
    }

    override fun updateStrategyForCustomer(customerId: String, strategyId: String) {
        reBalancingService.updateStrategyForCustomer(customerId,strategyId)
    }

    override fun getPortfolioForCustomer(customerId: String): CustomerPortfolioDto {
        return reBalancingService.getPortfolioForCustomer(customerId)
    }

    override fun getTradesForRebalancingCustomerPortfolio(customerId: String): CustomerTradeDto {
        return reBalancingService.getTradesForRebalancingCustomerPortfolio(customerId)
    }

    override fun executeTradesForRebalancingCustomerPortfolio(customersRequest: CustomersRequest){
         reBalancingService.executeTradesForRebalancingCustomerPortfolio(customersRequest)
    }


}