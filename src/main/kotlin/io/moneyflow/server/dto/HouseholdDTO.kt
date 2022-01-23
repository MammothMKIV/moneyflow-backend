package io.moneyflow.server.dto

import javax.validation.constraints.NotNull
import javax.validation.constraints.Null

data class HouseholdDTO(
    @Null
    var id: Long?,

    @NotNull
    var name: String,
)