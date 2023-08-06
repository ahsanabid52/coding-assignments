package de.element.travelguideservice.hiketrail

import de.element.travelguideservice.booking.HikeTrailsDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*

@RequestMapping("/api/trails")
interface HikeTrailsResource {

    @GetMapping("/all")
    fun getAllTrails(): ResponseEntity<List<HikeTrailsDto>>


    @GetMapping("/{id}")
    fun getSpecificHikeTrail(@PathVariable("id") id: UUID): ResponseEntity<HikeTrailsDto>

}