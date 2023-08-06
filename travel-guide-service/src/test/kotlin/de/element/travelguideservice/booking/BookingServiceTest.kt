package de.element.travelguideservice.booking

import de.element.travelguideservice.booking.persistence.BookingEntity
import de.element.travelguideservice.booking.persistence.BookingRepository
import de.element.travelguideservice.hiker.persistence.HikerRepository
import de.element.travelguideservice.hiketrail.HikeTrailsService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*
import org.mockito.Mockito.`when` as When


@ExtendWith(MockitoExtension::class)
internal class BookingServiceTest(
    @Mock private val bookingRepository: BookingRepository,
    @Mock private val hikerRepository: HikerRepository,
    @Mock private val hikeTrailsService: HikeTrailsService
) {
    @InjectMocks
    private lateinit var sut: BookingService


    @Test
    internal fun `should throw BookingNotFoundException for a non existing booking`() {
        assertThrows<BookingNotFoundException> {
            sut.getBooking(UUID.randomUUID())
        }
    }

    @Test
    internal fun `should throw BookingNotFoundException for a non existing booking while doing cancellation`() {
        assertThrows<BookingNotFoundException> {
            sut.cancelBooking(UUID.randomUUID())
        }
    }


    @Test
    internal fun `should not throw any Exception when looking for all bookings`() {
        assertDoesNotThrow {
            sut.getAllBookings()
        }
    }


    @Test
    internal fun `should get a booking with a valid booking id`() {
        val bookingId = UUID.randomUUID()
        When(bookingRepository.findById(bookingId)).thenReturn(
            Optional.of(
                BookingEntity(
                    id = bookingId,
                    hikeTrailId = UUID.randomUUID(),
                    createdAt = ZonedDateTime.now()
                )
            )
        )

        val actual = sut.getBooking(bookingId)

        assertThat(actual.id).isEqualTo(bookingId)

    }

    @Test
    internal fun `should create a booking for a valid booking request`() {
        val bookingId = UUID.randomUUID()
        val hikeTrailId = UUID.randomUUID()

        When(hikeTrailsService.getSpecificHikeTrail(hikeTrailId)).thenReturn(
            HikeTrailsDto(
                id = hikeTrailId, name = "Shire", startAt = "07:00", endAt = "09:00",
                minimumAge = 5, maximumAge = 100, unitPrice = BigDecimal("29.90")
            )
        )
        val bookingRequest = BookingRequest(
            hikers = listOf(HikerRequest(firstName = "ahsan", lastName = "abid", phoneNumber = "123", age = 20)),
            hikeTrailId = hikeTrailId
        )

        When(bookingRepository.save(any())).thenReturn(
            BookingEntity(id = bookingId, hikeTrailId = hikeTrailId, createdAt = ZonedDateTime.now())
        )

        val actual = sut.createBooking(bookingRequest = bookingRequest)

        assertThat(actual.id).isEqualTo(bookingId)

    }


    @Test
    internal fun `should throw AgeLimitException while creating a booking for a an invalid booking request`() {
        val hikeTrailId = UUID.randomUUID()
        When(hikeTrailsService.getSpecificHikeTrail(hikeTrailId)).thenReturn(
            HikeTrailsDto(
                id = hikeTrailId, name = "Shire", startAt = "07:00", endAt = "09:00",
                minimumAge = 18, maximumAge = 100, unitPrice = BigDecimal("29.90")
            )
        )
        val bookingRequest = BookingRequest(
            hikers = listOf(HikerRequest(firstName = "ahsan", lastName = "abid", phoneNumber = "123", age = 17)),
            hikeTrailId = hikeTrailId
        )


        assertThrows<AgeLimitException> {
            sut.createBooking(bookingRequest = bookingRequest)
        }

    }

}