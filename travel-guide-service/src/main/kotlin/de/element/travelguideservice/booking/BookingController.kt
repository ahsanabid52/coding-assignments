package de.element.travelguideservice.booking

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class BookingController(private val service: BookingService) : BookingResource {

    override fun createBooking(bookingRequest: BookingRequest): ResponseEntity<BookingDto> {
        return ResponseEntity.ok(service.createBooking(bookingRequest))
    }

    override fun viewBooking(id: UUID): ResponseEntity<BookingDto> {
        return ResponseEntity.ok(service.getBooking(id))
    }

    override fun cancelBooking(id: UUID) {
        service.cancelBooking(id)
    }

    override fun viewAllBookings(): ResponseEntity<List<BookingDto>> {
        return ResponseEntity.ok(service.getAllBookings())

    }
}