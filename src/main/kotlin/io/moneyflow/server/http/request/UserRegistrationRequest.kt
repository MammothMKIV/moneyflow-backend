package io.moneyflow.server.http.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UserRegistrationRequest(
    val firstName: String?,

    val lastName: String?,

    @Email
    @NotBlank
    val email: String,

    @NotBlank
    @Size(min = 6)
    val password: String
)