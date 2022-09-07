package io.moneyflow.server.helper

import io.moneyflow.server.response.ErrorApiResponse

class ApiResponseBuilder {
    companion object {
        fun buildValidationError(errors: Map<String, String>): ErrorApiResponse {
            return ErrorApiResponse("VALIDATION_ERROR", "Invalid request data", errors)
        }
    }
}