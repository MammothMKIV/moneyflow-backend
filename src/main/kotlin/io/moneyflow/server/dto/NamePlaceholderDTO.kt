package io.moneyflow.server.dto

import io.moneyflow.server.entity.NamePlaceholderType
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class NamePlaceholderDTO(
    val id: Long,

    @field:NotBlank(message = "Name must not be empty")
    var name: String,

    @field:NotNull(message = "Type must not be empty")
    var type: NamePlaceholderType,

    @field:NotNull(message = "Household must not be empty")
    var household: Long,
)