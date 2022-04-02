package io.moneyflow.server.dto

import javax.validation.constraints.NotNull
import io.moneyflow.server.entity.CategoryType
import javax.validation.constraints.NotBlank

data class CategoryDTO(
    var id: Long?,

    @field:NotBlank(message = "Color must not be empty")
    var color: String,

    @field:NotBlank(message = "Icon must not be empty")
    var icon: String,

    @field:NotBlank(message = "Name must not be empty")
    var name: String,

    @field:NotNull(message = "Type must not be empty")
    var type: CategoryType,

    @field:NotNull(message = "Household must not be empty")
    var household: Long,
)