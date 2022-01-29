package io.moneyflow.server.mapper

import io.moneyflow.server.dto.UserProfileDTO
import io.moneyflow.server.entity.User
import io.moneyflow.server.http.request.UserRegistrationRequest
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings

@Mapper(
    componentModel = "spring",
    uses = [
        AccountMapper::class,
        CategoryMapper::class,
        UserMapperPasswordEncoder::class
    ]
)
interface UserMapper {
    fun map(user: User): UserProfileDTO

    fun map(userProfileDTO: UserProfileDTO): User

    @Mappings(
        value = [
            Mapping(target = "password", qualifiedBy = [EncodePasswordMapper::class])
        ]
    )
    fun map(userRegistrationRequest: UserRegistrationRequest): User

    fun merge(transactionDTO: UserProfileDTO, @MappingTarget user: User): User
}