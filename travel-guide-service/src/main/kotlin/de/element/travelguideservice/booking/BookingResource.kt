package de.element.travelguideservice.booking

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RequestMapping("/api/booking")
interface BookingResource {

    @PostMapping
    fun createBooking(@RequestBody @Valid bookingRequest: BookingRequest): ResponseEntity<BookingDto>

    @GetMapping("/{id}")
    fun viewBooking(@PathVariable("id") id: UUID): ResponseEntity<BookingDto>


    @PostMapping("/cancel/{id}")
    fun cancelBooking(@PathVariable("id") id: UUID)


    @GetMapping("/all")
    fun viewAllBookings(): ResponseEntity<List<BookingDto>>


}