package io.moneyflow.server.http.request

import javax.validation.constraints.NotBlank

data class UserLoginRequest(
    @field:NotBlank(message = "Email must not be empty")
    val email: String,

    @field:NotBlank(message = "Password must not be empty")
    val password: String
)