package de.element.travelguideservice.booking

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty


data class HikeTrailsDto(
    val id: UUID,
    val name: String,
    val startAt: String,
    val endAt: String,
    val minimumAge: Int,
    val maximumAge: Int,
    val unitPrice: BigDecimal
)


data class HikerDto(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val age: Int
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BookingDto(
    val id: UUID,
    var hikers: List<HikerDto>? = null,
    var hikeTrail: HikeTrailsDto? = null,
    val createdAt: String
)


data class HikerRequest(
    @field:NotBlank
    val firstName: String,
    @field:NotBlank
    val lastName: String,
    @field:NotBlank
    val phoneNumber: String,
    @field:Min(1)
    val age: Int
)


data class BookingRequest(
    @field:Valid
    @field:NotEmpty
    val hikers: List<HikerRequest>,

    @field:Valid
    val hikeTrailId: UUID
)