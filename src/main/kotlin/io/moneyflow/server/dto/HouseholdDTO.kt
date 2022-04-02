package io.moneyflow.server.dto

import javax.validation.constraints.NotBlank

data class HouseholdDTO(
    var id: Long?,

    @field:NotBlank(message = "Name must not be empty")
    var name: String,
)