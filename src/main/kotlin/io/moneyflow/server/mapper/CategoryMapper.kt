package io.moneyflow.server.mapper

import io.moneyflow.server.dto.CategoryDTO
import io.moneyflow.server.entity.Category
import io.moneyflow.server.entity.Household
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget

@Mapper(
    componentModel = "spring",
    uses = [
        ReferenceMapper::class,
        HouseholdMapper::class
    ],
)
interface CategoryMapper {
    fun toEntity(id: Long?): Category?

    fun map(category: Category): CategoryDTO

    fun map(categoryDTO: CategoryDTO): Category

    fun merge(categoryDTO: CategoryDTO, @MappingTarget category: Category): Category

    fun householdId(household: Household?): Long? {
        return household?.id
    }
}