package de.element.travelguideservice.booking.persistence

import de.element.travelguideservice.hiker.persistence.HikerEntity
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "booking")
data class BookingEntity(

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    val id: UUID? = null,

    @Column(updatable = false, nullable = false)
    @OneToMany(mappedBy = "booking", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var hikers: List<HikerEntity> = emptyList(),

    @Column(updatable = false, nullable = false)
    var hikeTrailId: UUID,

    @Column(updatable = false, nullable = false)
    @CreatedDate
    val createdAt: ZonedDateTime,
)