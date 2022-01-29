package io.moneyflow.server.response

data class SuccessApiResponse<T>(
    val data: T?,
) : ApiResponse()