package io.moneyflow.server.mapper

import io.moneyflow.server.dto.HouseholdDTO
import io.moneyflow.server.dto.TransactionDTO
import io.moneyflow.server.entity.Transaction
import io.moneyflow.server.entity.Account
import io.moneyflow.server.entity.Category
import io.moneyflow.server.entity.Household
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import java.util.Optional

@Mapper(
    componentModel = "spring"
)
interface HouseholdMapper {
    fun map(household: Household): HouseholdDTO

    fun map(householdDTO: HouseholdDTO): Household
}