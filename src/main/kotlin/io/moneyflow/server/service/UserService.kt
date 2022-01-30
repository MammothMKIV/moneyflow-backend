package io.moneyflow.server.service

import io.moneyflow.server.entity.User
import io.moneyflow.server.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsService {
    fun register(user: User): User {
        user.createdAt = LocalDateTime.now()

        save(user)

        return user
    }

    fun getAll(page: Int, perPage: Int): Page<User> {
        return userRepository.findAll(PageRequest.of(page, perPage, Sort.by("createdAt").descending()))
    }

    fun save(user: User) {
        userRepository.save(user)
    }

    fun get(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun delete(id: Long) {
        userRepository.deleteById(id)
    }

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByEmail(username).orElse(null) ?: throw UsernameNotFoundException("User not found")
    }

    fun updatePassword(user: User, oldPassword: String, newPassword: String) {
        if (!passwordEncoder.matches(oldPassword, user.password)) {
            throw BadCredentialsException("Invalid current password")
        }

        user.password = passwordEncoder.encode(newPassword)

        save(user)
    }
}