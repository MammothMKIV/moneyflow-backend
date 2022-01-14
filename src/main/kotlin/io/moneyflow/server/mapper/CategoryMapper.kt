package io.moneyflow.server.mapper

import io.moneyflow.server.entity.Category
import org.mapstruct.Mapper

@Mapper(
    componentModel = "spring",
    uses = [
        ReferenceMapper::class
    ]
)
interface CategoryMapper {
    fun toEntity(id: Long?): Category?
}