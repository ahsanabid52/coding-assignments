package de.element.travelguideservice.hiketrail

import de.element.travelguideservice.hiketrail.persistence.HikeTrailsEntity
import de.element.travelguideservice.hiketrail.persistence.HikeTrailsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalTime
import java.util.*
import org.mockito.Mockito.`when` as When


@ExtendWith(MockitoExtension::class)
internal class HikeTrailsServiceTest(
    @Mock private val hikeTrailsRepository: HikeTrailsRepository
) {
    @InjectMocks
    private lateinit var sut: HikeTrailsService


    @Test
    internal fun `should throw HikeTrailNotFoundException for a non existing hike trail`() {
        assertThrows<HikeTrailNotFoundException> {
            sut.getSpecificHikeTrail(UUID.randomUUID())
        }
    }


    @Test
    internal fun `should not throw any Exception when looking for all hike trails`() {
        assertDoesNotThrow {
            sut.getAllHikeTrails()
        }
    }


    @Test
    internal fun `should get a hike trail with a valid hike trail id`() {
        val hikeTrailId = UUID.randomUUID()

        When(hikeTrailsRepository.findById(hikeTrailId)).thenReturn(
            Optional.of(
                HikeTrailsEntity(
                    id = hikeTrailId,
                    name = "Mordor", startAt = LocalTime.of(14, 0),
                    endAt = LocalTime.of(19, 0),
                    minimumAge = 18, maximumAge = 40, unitPrice = BigDecimal("99.90")
                )
            )
        )

        val actual = sut.getSpecificHikeTrail(hikeTrailId)

        assertThat(actual.id).isEqualTo(hikeTrailId)

    }


}