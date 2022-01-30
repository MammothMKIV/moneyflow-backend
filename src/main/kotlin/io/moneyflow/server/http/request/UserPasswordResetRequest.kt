package io.moneyflow.server.http.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class UserPasswordResetRequest(
    @NotBlank
    @Email
    val email: String,
)