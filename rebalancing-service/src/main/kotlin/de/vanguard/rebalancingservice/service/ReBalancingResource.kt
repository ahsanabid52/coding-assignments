package de.vanguard.rebalancingservice.service

import de.vanguard.rebalancingservice.service.client.CustomerPortfolioDto
import de.vanguard.rebalancingservice.service.client.CustomerTradeDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping("/api/re-balancing")
interface ReBalancingResource {

    @PostMapping("/load/daily/customers-strategies")
    @ResponseStatus(HttpStatus.CREATED)
    fun loadDailyCustomersAndStrategies()

    @GetMapping("/customers/all")
    fun getAllCustomers(): List<CustomerDto>

    @GetMapping("/strategies/all")
    fun getAllStrategies(): List<StrategyDto>

    @GetMapping("/customer/{customerId}/strategies/")
    fun getStrategiesSuitableForCustomer(@PathVariable("customerId") customerId: String): List<StrategyDto>

    @PostMapping("/customer/{customerId}/strategy/{strategyId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateStrategyForCustomer(@PathVariable("customerId") customerId: String, @PathVariable("strategyId") strategyId: String)

    @GetMapping("/customer/{customerId}/portfolio")
    fun getPortfolioForCustomer(@PathVariable("customerId") customerId: String): CustomerPortfolioDto

    @GetMapping("/customer/{customerId}/portfolio/trades")
    fun getTradesForRebalancingCustomerPortfolio(@PathVariable("customerId") customerId: String): CustomerTradeDto

    @PostMapping("/customers/portfolio/trades")
    @ResponseStatus(HttpStatus.CREATED)
    fun executeTradesForRebalancingCustomerPortfolio(@RequestBody @Valid customersRequest: CustomersRequest)

}