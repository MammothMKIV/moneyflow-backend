package io.moneyflow.server.http.response

import io.moneyflow.server.dto.UserProfileDTO

data class UserLoginResponse(
    val token: String,
    val profile: UserProfileDTO
)