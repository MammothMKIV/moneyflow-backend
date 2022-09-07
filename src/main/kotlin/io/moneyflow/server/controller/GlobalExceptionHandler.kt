package io.moneyflow.server.controller

import io.moneyflow.server.helper.ApiResponseBuilder
import io.moneyflow.server.helper.ValidationHelper
import io.moneyflow.server.response.ErrorApiResponse
import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
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
        return ApiResponseBuilder.buildValidationError(ValidationHelper.bindingResultToErrorMap(e.bindingResult))
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(MissingPathVariableException::class)
    fun handleValidationExceptions(e: MissingPathVariableException) {

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(BindException::class)
    fun handleBindingException(e: BindException): ErrorApiResponse {
        val errors = HashMap<String, String>()

        e.bindingResult.allErrors.forEach { error -> run {
            val fieldName = (error as FieldError).field

            errors[fieldName] = "Invalid value: " + error.rejectedValue
        } }

        return ApiResponseBuilder.buildValidationError(errors)
    }
}