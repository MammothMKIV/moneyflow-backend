package io.moneyflow.server.dto

import java.time.LocalDateTime
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Null

data class UserProfileDTO(
    @Null
    val id: Long?,

    val firstName: String?,

    val lastName: String?,

    @Email
    @NotEmpty
    val email: String,

    @Null
    val createdAt: LocalDateTime?
)