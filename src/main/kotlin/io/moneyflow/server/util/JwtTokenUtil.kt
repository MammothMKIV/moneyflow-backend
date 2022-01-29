package io.moneyflow.server.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenUtil(
    @Value("\${jwt.secret}") val secret: String
) {
    fun parseToken(token: String): AuthTokenCredentials {
        val body: Claims = Jwts.parserBuilder()
            .setSigningKey(secret)
            .build()
            .parseClaimsJws(token)
            .body

        val userId = (body["userId"] as Int).toLong()

        return AuthTokenCredentials(userId)
    }

    fun generateToken(credentials: AuthTokenCredentials): String {
        val claims: Claims = Jwts.claims().setSubject("subject")

        claims["userId"] = credentials.userId

        return Jwts.builder()
            .setClaims(claims)
            .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret)))
            .compact()
    }
}