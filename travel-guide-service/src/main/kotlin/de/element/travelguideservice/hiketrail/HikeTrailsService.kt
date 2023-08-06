package de.element.travelguideservice.hiketrail

import de.element.travelguideservice.booking.HikeTrailsDto
import de.element.travelguideservice.hiketrail.persistence.HikeTrailsRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class HikeTrailsService(private val hikeTrailsRepository: HikeTrailsRepository) {

    fun getAllHikeTrails(): List<HikeTrailsDto> {
        return hikeTrailsRepository.findAll().map { it.toDto() }
    }


    fun getSpecificHikeTrail(id: UUID): HikeTrailsDto {
        return hikeTrailsRepository.findById(id).orElseThrow { HikeTrailNotFoundException() }
            .toDto()
    }
}