package io.moneyflow.server.http.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UserPasswordResetConfirmRequest(
    @field:NotBlank(message = "Token must not be empty")
    val token: String,

    @field:NotBlank(message = "Password must not be empty")
    @field:Size(min = 6, message = "Password must be at least 6 characters long")
    val password: String,
)