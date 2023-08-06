package de.vanguard.rebalancingservice.service.persistence

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StrategyRepository : CrudRepository<StrategyEntity, UUID> {

    fun findByStrategyId(strategyId: String): Optional<StrategyEntity>

}