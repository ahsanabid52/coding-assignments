package de.vanguard.rebalancingservice.service.persistence

import de.vanguard.rebalancingservice.service.CustomerDto
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "customers")
data class CustomerEntity(

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    var id: UUID? = null,

    @Column(updatable = false, nullable = false)
    var customerId: String,

    @Column(updatable = false, nullable = false)
    var email: String,

    @Column(updatable = false, nullable = false)
    var riskLevel: Int,

    @Column(updatable = false, nullable = false)
    var retirementAge: Int,

    @Column(updatable = false, nullable = false)
    @CreatedDate
    var dateOfBirth: LocalDate,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "selected_strategy", referencedColumnName = "id")
    var selectedStrategy: StrategyEntity? = null,

    @Column(updatable = false, nullable = false)
    @CreatedDate
    var createdAt: ZonedDateTime? = null
) {
    fun toDto() = CustomerDto(
        id = id.toString(),
        customerId = customerId,
        email = email,
        riskLevel = riskLevel,
        retirementAge = retirementAge,
        selectedStrategy = selectedStrategy?.toDto(),
        dateOfBirth = dateOfBirth.toString(),
        createdAt = createdAt.toString()
    )
}