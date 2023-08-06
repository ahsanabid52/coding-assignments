package de.vanguard.rebalancingservice.config


import de.vanguard.rebalancingservice.config.ErrorResponseEntity.Companion.badRequest
import de.vanguard.rebalancingservice.config.ErrorResponseEntity.Companion.clientError
import de.vanguard.rebalancingservice.config.ErrorResponseEntity.Companion.notFound
import de.vanguard.rebalancingservice.config.ErrorResponseEntity.Companion.serverError
import de.vanguard.rebalancingservice.service.exceptions.*
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {


    @ExceptionHandler(value = [CustomerNotFoundException::class, StrategyNotFoundException::class, CustomerPortfolioNotFoundException::class])
    fun handleNotFoundException(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        return notFound(ex.message)
    }

    @ExceptionHandler(value = [NoStrategySelectedForCustomerException::class, InvalidStrategyProvidedForTheCustomer::class, UnableToExecuteCustomerTradesException::class, BatchSizeLimitForCustomerTradesExceededException::class])
    fun handleBadRequestException(ex: Exception, request: WebRequest): ErrorResponseEntity {
        return badRequest(ex.message)
    }


    @ExceptionHandler(value = [ResponseStatusException::class])
    fun handleResponseStatusExceptions(
        ex: ResponseStatusException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        return if (ex.status.is4xxClientError) clientError(
            ex.status,
            ex.message,
        ) else serverError(ex.status, ex.message)
    }

    @ExceptionHandler(value = [Exception::class, CSVParserFailedException::class])
    fun handleGenericException(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        return serverError(ex.message)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val errors = ex.bindingResult.allErrors.map { objectError ->
            if (objectError is FieldError) {
                "'${objectError.field}:${objectError.rejectedValue}' ${objectError.defaultMessage}"
            } else {
                objectError.defaultMessage
            }
        }
        return handleExceptionInternal(
            ex,
            ErrorResponse(status, errors.joinToString()),
            headers,
            status,
            request
        )
    }
}