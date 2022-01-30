package io.moneyflow.server.http.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UserPasswordUpdateRequest(
    @NotBlank
    val oldPassword: String,

    @NotBlank
    @Size(min = 6)
    val newPassword: String
)