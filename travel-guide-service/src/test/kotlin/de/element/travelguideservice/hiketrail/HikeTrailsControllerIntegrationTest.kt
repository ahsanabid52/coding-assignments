package de.element.travelguideservice.hiketrail

import de.element.travelguideservice.AbstractIntegrationTest
import de.element.travelguideservice.booking.HikeTrailsDto
import de.element.travelguideservice.config.ErrorResponse
import de.element.travelguideservice.hiketrail.persistence.HikeTrailsEntity
import de.element.travelguideservice.hiketrail.persistence.HikeTrailsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalTime
import java.util.*


internal class HikeTrailsControllerIntegrationTest @Autowired constructor(
    private val restTemplate: TestRestTemplate,
    private val hikeTrailsRepository: HikeTrailsRepository
) : AbstractIntegrationTest() {

    private val url = "/api/trails"

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
    internal fun `should return NOT_FOUND when the hike trails is not found in db for the id`() {
        val actual = restTemplate.exchange(
            url.plus("/").plus(UUID.randomUUID()), HttpMethod.GET, null, ErrorResponse::class.java
        )

        assertThat(actual.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }


    @Test
    internal fun `should return a list of trails`() {
        val actual = restTemplate.exchange(
            url.plus("/all"), HttpMethod.GET, null, Array<HikeTrailsDto>::class.java
        )

        assertThat(actual.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(actual.body!!.size).isEqualTo(3)

    }


    @Test
    internal fun `should return a hike trail when the hike trails is valid`() {
        val hikeTrailsResposne = restTemplate.exchange(
            url.plus("/all"), HttpMethod.GET, null, Array<HikeTrailsDto>::class.java
        )

        assertThat(hikeTrailsResposne.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(hikeTrailsResposne.body!!.size).isEqualTo(3)
        val hikeTrailId = hikeTrailsResposne.body!!.first().id

        val actual = restTemplate.exchange(
            url.plus("/").plus(hikeTrailId), HttpMethod.GET, null, HikeTrailsDto::class.java
        )

        assertThat(actual.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(actual.body!!.id).isEqualTo(hikeTrailId)


    }


}


