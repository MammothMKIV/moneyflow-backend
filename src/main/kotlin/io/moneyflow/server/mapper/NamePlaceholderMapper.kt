package io.moneyflow.server.mapper

import io.moneyflow.server.dto.NamePlaceholderDTO
import io.moneyflow.server.entity.Household
import io.moneyflow.server.entity.NamePlaceholder
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings

@Mapper(
    componentModel = "spring",
    uses = [
        ReferenceMapper::class,
        HouseholdMapper::class
    ]
)
interface NamePlaceholderMapper {
    fun toEntity(id: Long?): NamePlaceholder?

    fun map(namePlaceholder: NamePlaceholder): NamePlaceholderDTO

    fun map(namePlaceholderDTO: NamePlaceholderDTO): NamePlaceholder

    @Mappings(
        value = [
            Mapping(target = "type", ignore = true),
            Mapping(target = "household", ignore = true)
        ]
    )
    fun merge(namePlaceholderDTO: NamePlaceholderDTO, @MappingTarget namePlaceholder: NamePlaceholder): NamePlaceholder

    fun householdId(household: Household?): Long? {
        return household?.id
    }
}