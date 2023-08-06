package de.element.travelguideservice.booking.persistence

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BookingRepository : CrudRepository<BookingEntity, UUID>