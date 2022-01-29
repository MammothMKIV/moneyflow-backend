package io.moneyflow.server.http.request

data class UserLoginRequest(
    val email: String,
    val password: String
)