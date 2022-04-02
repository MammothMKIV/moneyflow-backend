package io.moneyflow.server.http.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UserPasswordUpdateRequest(
    @field:NotBlank(message = "Old password must not be empty")
    val oldPassword: String,

    @field:NotBlank(message = "New password must not be empty")
    @field:Size(min = 6, message = "New password must be ar least 6 characters long")
    val newPassword: String
)