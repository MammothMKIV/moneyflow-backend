package io.moneyflow.server.http.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UserPasswordResetConfirmRequest(
    @NotBlank
    val token: String,

    @NotBlank
    @Size(min = 6)
    val password: String,
)