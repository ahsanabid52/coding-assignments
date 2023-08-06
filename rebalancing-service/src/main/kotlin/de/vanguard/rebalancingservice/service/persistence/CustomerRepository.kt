package de.vanguard.rebalancingservice.service.persistence

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CustomerRepository : CrudRepository<CustomerEntity, UUID> {

    fun findByCustomerId(customerId: String): Optional<CustomerEntity>
}