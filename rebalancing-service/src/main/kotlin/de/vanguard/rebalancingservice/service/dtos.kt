package de.vanguard.rebalancingservice.service

import java.math.BigDecimal
import javax.validation.Valid
import javax.validation.constraints.NotEmpty


data class CustomerDto(
    val id: String,
    val customerId: String,
    val email: String,
    val riskLevel: Int,
    val retirementAge: Int,
    val selectedStrategy: StrategyDto? = null,
    val dateOfBirth: String,
    val createdAt: String
)


data class StrategyDto(
    val id: String,
    val strategyId: String,
    val minRiskLevel: Int,
    val maxRiskLevel: Int,
    val minYearsToRetirement: Int,
    val maxYearsToRetirement: Int,
    val stocksPercentage: BigDecimal,
    val cashPercentage: BigDecimal,
    val bondsPercentage: BigDecimal,
    val createdAt: String
)


data class CustomersRequest(
    @field:Valid
    @field:NotEmpty
    val customers: List<String>
)
