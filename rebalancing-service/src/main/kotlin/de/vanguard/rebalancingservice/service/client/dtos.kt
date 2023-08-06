package de.vanguard.rebalancingservice.service.client

import java.math.BigDecimal

data class CustomerPortfolioDto(
    val customerId: String,
    val stocks: BigDecimal,
    val bonds: BigDecimal,
    val cash: BigDecimal
)


data class CustomerTradeDto(
    val customerId: String,
    val stocks: BigDecimal,
    val bonds: BigDecimal,
    val cash: BigDecimal
)

data class CustomerPortfolioStats(
    val customerId: String,
    val totalAssets: BigDecimal,
    val stocksPercentage: BigDecimal,
    val bondsPercentage: BigDecimal,
    val cashPercentage: BigDecimal
)