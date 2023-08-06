package de.element.travelguideservice.booking

import de.element.travelguideservice.AbstractIntegrationTest
import de.element.travelguideservice.config.ErrorResponse
import de.element.travelguideservice.hiketrail.HikeTrailsService
import de.element.travelguideservice.hiketrail.persistence.HikeTrailsEntity
import de.element.travelguideservice.hiketrail.persistence.HikeTrailsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalTime
import java.util.*


internal class BookingControllerIntegrationTest @Autowired constructor(
    private val restTemplate: TestRestTemplate,
    private val hikeTrailsService: HikeTrailsService,
    private val hikeTrailsRepository: HikeTrailsRepository
) : AbstractIntegrationTest() {

    private val url = "/api/booking"

    @BeforeEach
    fun setupHikeTrails() {
        hikeTrailsRepository.save(
            HikeTrailsEntity(
                id = UUID.randomUUID(),
                name = "Shire", startAt = LocalTime.of(7, 0),
                endAt = LocalTime.of(9, 0),
                minimumAge = 5, maximumAge = 100, unitPrice = BigDecimal("29.90")
            )
        )
        hikeTrailsRepository.save(
            HikeTrailsEntity(
                id = UUID.randomUUID(),
                name = "Gondor", startAt = LocalTime.of(10, 0),
                endAt = LocalTime.of(13, 0),
                minimumAge = 11, maximumAge = 50, unitPrice = BigDecimal("59.90")
            )
        )
        hikeTrailsRepository.save(
            HikeTrailsEntity(
                id = UUID.randomUUID(),
                name = "Mordor", startAt = LocalTime.of(14, 0),
                endAt = LocalTime.of(19, 0),
                minimumAge = 18, maximumAge = 40, unitPrice = BigDecimal("99.90")
            )
        )
    }

    @Test
    internal fun `should return NOT_FOUND when the booking is not found in db for the id`() {
        val actual = restTemplate.exchange(
            url.plus("/").plus(UUID.randomUUID()), HttpMethod.GET, null, ErrorResponse::class.java
        )

        assertThat(actual.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }


    @Test
    internal fun `should return nothing when there are no bookings in db`() {
        val actual = restTemplate.exchange(
            url.plus("/all"), HttpMethod.GET, null,
            Void::class.java
        )

        assertThat(actual.statusCode).isEqualTo(HttpStatus.OK)
    }


    @Test
    internal fun `should create a booking for a valid booking request`() {
        val hikeTrailId = hikeTrailsService.getAllHikeTrails().first().id
        val bookingRequest = BookingRequest(
            hikers = listOf(HikerRequest(firstName = "ahsan", lastName = "abid", phoneNumber = "123", age = 20)),
            hikeTrailId = hikeTrailId
        )
        val request = HttpEntity<Any>(bookingRequest, null)

        val actual = restTemplate.exchange(
            url, HttpMethod.POST, request,
            BookingDto::class.java
        )

        assertThat(actual.statusCode).isEqualTo(HttpStatus.OK)
    }


    @Test
    internal fun `should be able to get the booking details for a valid booking id`() {
        val hikeTrailId = hikeTrailsService.getAllHikeTrails().first().id
        val bookingRequest = BookingRequest(
            hikers = listOf(HikerRequest(firstName = "ahsan", lastName = "abid", phoneNumber = "123", age = 20)),
            hikeTrailId = hikeTrailId
        )
        val request = HttpEntity<Any>(bookingRequest, null)

        val bookingCreated = restTemplate.exchange(
            url, HttpMethod.POST, request,
            BookingDto::class.java
        )
        assertThat(bookingCreated.statusCode).isEqualTo(HttpStatus.OK)


        val actual = restTemplate.exchange(
            url.plus("/").plus(bookingCreated.body!!.id), HttpMethod.GET, null, BookingDto::class.java
        )
        assertThat(actual.statusCode).isEqualTo(HttpStatus.OK)


        val expectedHikerDto =
            HikerDto(id = UUID.randomUUID(), firstName = "ahsan", lastName = "abid", phoneNumber = "123", age = 20)

        val expectedHikeTrailDto = hikeTrailsService.getAllHikeTrails().first()

        assertThat(actual.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(actual.body!!.hikers!!.first()).isEqualToIgnoringGivenFields(expectedHikerDto, "id")
        assertThat(actual.body!!.hikeTrail!!).isEqualTo(expectedHikeTrailDto)

    }


    @Test
    internal fun `should be able to cancel an existing booking for a valid booking id`() {
        val hikeTrailId = hikeTrailsService.getAllHikeTrails().first().id
        val bookingRequest = BookingRequest(
            hikers = listOf(HikerRequest(firstName = "ahsan", lastName = "abid", phoneNumber = "123", age = 20)),
            hikeTrailId = hikeTrailId
        )
        val request = HttpEntity<Any>(bookingRequest, null)

        val actual = restTemplate.exchange(
            url, HttpMethod.POST, request,
            BookingDto::class.java
        )

        assertThat(actual.statusCode).isEqualTo(HttpStatus.OK)

        assertDoesNotThrow {
            restTemplate.exchange(
                url.plus("/cancel/").plus(actual.body!!.id), HttpMethod.POST, null,
                Void::class.java
            )
        }


    }


}