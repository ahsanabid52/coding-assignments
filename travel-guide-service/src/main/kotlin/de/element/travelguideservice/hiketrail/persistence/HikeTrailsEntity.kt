package de.element.travelguideservice.hiketrail.persistence

import de.element.travelguideservice.booking.HikeTrailsDto
import org.hibernate.annotations.GenericGenerator
import java.math.BigDecimal
import java.time.LocalTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "hike_trails")
data class HikeTrailsEntity(

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    val id: UUID? = null,

    @Column(updatable = false, nullable = false)
    val name: String,

    @Column(updatable = false, nullable = false)
    val startAt: LocalTime? = null,

    @Column(updatable = false, nullable = false)
    val endAt: LocalTime? = null,

    @Column(updatable = false, nullable = false)
    val minimumAge: Int,

    @Column(updatable = false, nullable = false)
    val maximumAge: Int,

    @Column(updatable = false, nullable = false)
    val unitPrice: BigDecimal
) {
    fun toDto() = HikeTrailsDto(
        id = id!!,
        name = name,
        startAt = startAt.toString(),
        endAt = endAt.toString(),
        minimumAge = minimumAge,
        maximumAge = maximumAge,
        unitPrice = unitPrice
    )
}