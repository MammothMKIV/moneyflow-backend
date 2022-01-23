package io.moneyflow.server.dto

import io.moneyflow.server.entity.NamePlaceholderType
import javax.validation.constraints.NotNull
import javax.validation.constraints.Null

data class NamePlaceholderDTO(
    @Null
    val id: Long,

    @NotNull
    var name: String,

    @NotNull
    var type: NamePlaceholderType,

    @NotNull
    var household: Long,
)