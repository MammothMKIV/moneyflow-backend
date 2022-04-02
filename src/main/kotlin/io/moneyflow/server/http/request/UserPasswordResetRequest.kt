package io.moneyflow.server.http.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class UserPasswordResetRequest(
    @field:NotBlank(message = "Email must not be empty")
    @field:Email(message = "Invalid email")
    val email: String,
)