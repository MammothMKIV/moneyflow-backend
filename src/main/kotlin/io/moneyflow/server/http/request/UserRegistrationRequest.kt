package io.moneyflow.server.http.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UserRegistrationRequest(
    val firstName: String?,

    val lastName: String?,

    @field:Email(message = "Invalid email")
    @field:NotBlank(message = "Email must not be empty")
    val email: String,

    @field:NotBlank(message = "Password must not be empty")
    @field:Size(min = 6, message = "Password must be at least 6 characters long")
    val password: String
)