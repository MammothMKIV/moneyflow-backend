package io.moneyflow.server.mapper

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserMapperPasswordEncoder(
    val passwordEncoder: PasswordEncoder
) {
    @EncodePasswordMapper
    fun encodePassword(password: String): String {
        return passwordEncoder.encode(password)
    }
}