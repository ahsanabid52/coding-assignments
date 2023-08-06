package de.element.travelguideservice.hiker.persistence

import de.element.travelguideservice.booking.HikerDto
import de.element.travelguideservice.booking.persistence.BookingEntity
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "hiker")
data class HikerEntity(

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    val id: UUID? = null,

    @Column(updatable = false, nullable = false)
    val firstName: String,

    @Column(updatable = false, nullable = false)
    val lastName: String,

    @Column(updatable = false, nullable = false)
    val phoneNumber: String,

    @Column(updatable = false, nullable = false)
    val age: Int,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id")
    var booking: BookingEntity? = null

) {
    fun toDto() = HikerDto(
        id = id!!,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        age = age
    )
}