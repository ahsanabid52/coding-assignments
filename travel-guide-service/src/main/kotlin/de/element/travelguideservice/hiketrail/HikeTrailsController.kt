package de.element.travelguideservice.hiketrail

import de.element.travelguideservice.booking.HikeTrailsDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class HikeTrailsController(private val service: HikeTrailsService) : HikeTrailsResource {

    override fun getAllTrails(): ResponseEntity<List<HikeTrailsDto>> {
        return ResponseEntity.ok(service.getAllHikeTrails())
    }

    override fun getSpecificHikeTrail(id: UUID): ResponseEntity<HikeTrailsDto> {
        return ResponseEntity.ok(service.getSpecificHikeTrail(id))

    }


}