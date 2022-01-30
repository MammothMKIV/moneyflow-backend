package io.moneyflow.server.response

class ErrorApiResponse(
    var error: String,
    var message: String?,
) : ApiResponse()