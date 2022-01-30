package io.moneyflow.server.service

import io.moneyflow.server.entity.OperationConfirmation
import io.moneyflow.server.entity.OperationConfirmationType
import io.moneyflow.server.entity.User
import io.moneyflow.server.repository.OperationConfirmationRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import java.util.*

@Service
class OperationConfirmationService(
    val operationConfirmationRepository: OperationConfirmationRepository,
    val passwordEncoder: PasswordEncoder,
) {
    data class Token(
        val series: String,
        val secret: String
    )

    fun issue(user: User, type: OperationConfirmationType, lifespan: Long): Token {
        val series = UUID.randomUUID().toString()
        val secret = passwordEncoder.encode(UUID.randomUUID().toString())
        val encodedSecret = passwordEncoder.encode(secret)
        val expiringAt = LocalDateTime.now().plusSeconds(lifespan)
        val operationConfirmation = OperationConfirmation(
            null,
            type,
            series,
            encodedSecret,
            expiringAt,
            user,
            null,
            LocalDateTime.now(),
            null
        )

        operationConfirmationRepository.save(operationConfirmation)

        return Token(series, secret)
    }

    fun get(token: Token, type: OperationConfirmationType): OperationConfirmation? {
        return operationConfirmationRepository.findBySeriesAndType(token.series, type).orElse(null)
    }

    fun purge(operationConfirmation: OperationConfirmation) {
        operationConfirmationRepository.delete(operationConfirmation)
    }

    fun verify(operationConfirmation: OperationConfirmation, token: Token) {
        if (!passwordEncoder.matches(token.secret, operationConfirmation.token) || token.series != operationConfirmation.series) {
            throw BadCredentialsException("Invalid token value")
        }

        if (operationConfirmation.expiringAt.isBefore(LocalDateTime.now())) {
            throw CredentialsExpiredException("Token has expired")
        }
    }

    fun encodeToken(token: Token): String {
        return Base64.getEncoder().encodeToString("${token.series}:${token.secret}".toByteArray())
    }

    fun decodeToken(encodedToken: String): Token {
        val decodedToken = String(Base64.getDecoder().decode(encodedToken))
        val tokenParts = decodedToken.split(":")

        if (tokenParts.size != 2) {
            throw IllegalArgumentException("Invalid token format")
        }

        return Token(tokenParts[0], tokenParts[1])
    }
}