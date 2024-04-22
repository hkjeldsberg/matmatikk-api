package no.matmatikk.api.exceptions

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler() {

    @ExceptionHandler(CustomRuntimeException::class)
    fun handleCustomRuntimeException(exception: CustomRuntimeException) = exception.toExceptionResponseMap()
}