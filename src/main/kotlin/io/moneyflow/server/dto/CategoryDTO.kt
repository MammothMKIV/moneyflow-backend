package io.moneyflow.server.dto
import com.sun.istack.NotNull
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