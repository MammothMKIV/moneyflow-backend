package io.moneyflow.server.mapper

import io.moneyflow.server.dto.HouseholdDTO
import io.moneyflow.server.entity.Household
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget

@Mapper(
    componentModel = "spring",
    uses = [
        ReferenceMapper::class
    ]
)
interface HouseholdMapper {
    fun toEntity(id: Long?): Household?

    fun map(household: Household): HouseholdDTO

    fun map(householdDTO: HouseholdDTO): Household

    fun merge(householdDTO: HouseholdDTO, @MappingTarget household: Household): Household
}