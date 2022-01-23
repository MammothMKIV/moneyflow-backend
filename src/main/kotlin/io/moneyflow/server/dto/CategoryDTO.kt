package io.moneyflow.server.dto

import javax.validation.constraints.NotNull
import io.moneyflow.server.entity.CategoryType
import javax.validation.constraints.Null

data class CategoryDTO(
    @Null
    var id: Long?,

    @NotNull
    var color: String,

    @NotNull
    var icon: String,

    @NotNull
    var name: String,

    @NotNull
    var type: CategoryType,

    @NotNull
    var household: Long,
)