package io.moneyflow.server.mapper

import io.moneyflow.server.entity.Account
import org.mapstruct.Mapper

@Mapper(
    componentModel = "spring",
    uses = [
        ReferenceMapper::class
    ]
)
interface AccountMapper {
    fun toEntity(id: Long?): Account?
}