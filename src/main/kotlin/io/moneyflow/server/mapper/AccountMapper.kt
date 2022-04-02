package io.moneyflow.server.mapper

import io.moneyflow.server.dto.AccountDTO
import io.moneyflow.server.entity.Account
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
interface AccountMapper {
    fun toEntity(id: Long?): Account?

    fun map(account: Account): AccountDTO

    @Mappings(
        value = [
            Mapping(target = "id", ignore = true)
        ]
    )
    fun map(accountDTO: AccountDTO): Account

    @Mappings(
        value = [
            Mapping(target = "id", ignore = true)
        ]
    )
    fun merge(accountDTO: AccountDTO, @MappingTarget account: Account): Account
}