package de.element.travelguideservice.booking

import de.element.travelguideservice.booking.persistence.BookingEntity
import de.element.travelguideservice.booking.persistence.BookingRepository
import de.element.travelguideservice.hiker.persistence.HikerEntity
import de.element.travelguideservice.hiker.persistence.HikerRepository
import de.element.travelguideservice.hiketrail.HikeTrailsService
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.*
import javax.transaction.Transactional

@Service
class BookingService(
    private val bookingRepository: BookingRepository, private val hikerRepository: HikerRepository,
    private val hikeTrailsService: HikeTrailsService
) {
    private val logger = KotlinLogging.logger {}

    @Transactional
    fun createBooking(bookingRequest: BookingRequest): BookingDto {
        logger.info { "Booking request received $bookingRequest" }

        val matchedHike = hikeTrailsService.getSpecificHikeTrail(bookingRequest.hikeTrailId)
        logger.info { "Valid Hike $matchedHike" }

        if (!hikersCanBookThis(bookingRequest.hikers, matchedHike.minimumAge, matchedHike.maximumAge)) {
            throw AgeLimitException()
        }

        val booking = bookingRepository.save(
            BookingEntity(
                hikeTrailId = matchedHike.id,
                createdAt = ZonedDateTime.now()
            )
        )

        hikerRepository.saveAll(
            bookingRequest.hikers.map {
                HikerEntity(
                    firstName = it.firstName,
                    lastName = it.lastName,
                    age = it.age,
                    phoneNumber = it.phoneNumber,
                    booking = booking
                )
            }.toList()
        )

        return BookingDto(id = booking.id!!, createdAt = booking.createdAt.toString())

    }

    fun getBooking(id: UUID): BookingDto {
        return getBookingDto(getBookingEntity(id = id))
    }

    @Transactional
    fun cancelBooking(id: UUID) {
        bookingRepository.delete(getBookingEntity(id = id))
    }

    private fun getBookingEntity(id: UUID): BookingEntity {
        return bookingRepository.findById(id).orElseThrow { BookingNotFoundException() }
    }

    private fun getBookingDto(existingBooking: BookingEntity): BookingDto {
        return BookingDto(
            id = existingBooking.id!!,
            hikeTrail = hikeTrailsService.getSpecificHikeTrail(existingBooking.hikeTrailId),
            hikers = existingBooking.hikers.map { it.toDto() }.toList(),
            createdAt = existingBooking.createdAt.toString()
        )
    }

    private fun hikersCanBookThis(hikers: List<HikerRequest>, minimumAge: Int, maximumAge: Int): Boolean {
        return hikers.filterNot { it.age in minimumAge..maximumAge }.isEmpty()
    }

    fun getAllBookings(): List<BookingDto> {
        return bookingRepository.findAll().map { getBookingDto(it) }.toList()
    }
}