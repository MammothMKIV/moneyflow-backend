package io.moneyflow.server.mapper

import io.moneyflow.server.dto.HouseholdDTO
import io.moneyflow.server.entity.Household
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings

@Mapper(
    componentModel = "spring",
    uses = [
        ReferenceMapper::class
    ]
)
interface HouseholdMapper {
    fun toEntity(id: Long?): Household?

    fun map(household: Household): HouseholdDTO

    @Mappings(
        value = [
            Mapping(target = "id", ignore = true)
        ]
    )
    fun map(householdDTO: HouseholdDTO): Household

    @Mappings(
        value = [
            Mapping(target = "id", ignore = true)
        ]
    )
    fun merge(householdDTO: HouseholdDTO, @MappingTarget household: Household): Household
}