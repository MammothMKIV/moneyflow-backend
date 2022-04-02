package io.moneyflow.server.controller

import io.moneyflow.server.response.ErrorApiResponse
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(e: MethodArgumentNotValidException): ErrorApiResponse {
        val errors = HashMap<String, String>()

        e.bindingResult.allErrors.forEach { error -> run {
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage

            errors[fieldName] = errorMessage!!
        } }

        return ErrorApiResponse("VALIDATION_ERROR", "Invalid request data", errors)
    }
}