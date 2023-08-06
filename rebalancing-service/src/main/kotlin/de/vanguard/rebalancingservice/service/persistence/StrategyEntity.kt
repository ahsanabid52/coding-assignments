package de.vanguard.rebalancingservice.service.persistence

import de.vanguard.rebalancingservice.service.StrategyDto
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "strategies")
data class StrategyEntity(

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    val id: UUID? = null,

    @Column(updatable = false, nullable = false)
    val strategyId: String,

    @Column(updatable = false, nullable = false)
    val minRiskLevel: Int,

    @Column(updatable = false, nullable = false)
    val maxRiskLevel: Int,

    @Column(updatable = false, nullable = false)
    val minYearsToRetirement: Int,

    @Column(updatable = false, nullable = false)
    val maxYearsToRetirement: Int,

    @Column(updatable = false, nullable = false)
    val stocksPercentage: BigDecimal,

    @Column(updatable = false, nullable = false)
    val cashPercentage: BigDecimal,

    @Column(updatable = false, nullable = false)
    val bondsPercentage: BigDecimal,

    @Column(updatable = false, nullable = false)
    @CreatedDate
    val createdAt: ZonedDateTime

){
    fun toDto() = StrategyDto(
        id = id.toString(),
        strategyId = strategyId,
        minRiskLevel = minRiskLevel,
        maxRiskLevel = maxRiskLevel,
        minYearsToRetirement = minYearsToRetirement,
        maxYearsToRetirement = maxYearsToRetirement,
        stocksPercentage = stocksPercentage,
        cashPercentage = cashPercentage,
        bondsPercentage = bondsPercentage,
        createdAt = createdAt.toString()
    )
}