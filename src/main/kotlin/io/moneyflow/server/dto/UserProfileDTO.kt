package io.moneyflow.server.dto

import java.time.LocalDateTime
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

data class UserProfileDTO(
    val id: Long?,

    val firstName: String?,

    val lastName: String?,

    @field:Email(message = "Invalid email")
    @field:NotBlank(message = "Email must not be empty")
    val email: String,

    val createdAt: LocalDateTime?
)