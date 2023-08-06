package de.vanguard.rebalancingservice.config

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.ZonedDateTime

data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String?,
    val timestamp: ZonedDateTime = ZonedDateTime.now()
) {
    constructor(status: HttpStatus, message: String?) : this(status.value(), status.reasonPhrase, message)
}

class ErrorResponseEntity(body: ErrorResponse) : ResponseEntity<ErrorResponse>(body, HttpStatus.valueOf(body.status)) {

    companion object {
        fun badRequest(message: String?) = clientError(HttpStatus.BAD_REQUEST, message)
        fun notFound(message: String?) = clientError(HttpStatus.NOT_FOUND, message)
        fun clientError(httpStatus: HttpStatus = HttpStatus.BAD_REQUEST, message: String?) =
            ErrorResponseEntity(ErrorResponse(httpStatus, message))

        fun serverError(message: String?) = serverError(HttpStatus.INTERNAL_SERVER_ERROR, message)
        fun serverError(httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR, message: String?) =
            ErrorResponseEntity(ErrorResponse(httpStatus, message))
    }
}