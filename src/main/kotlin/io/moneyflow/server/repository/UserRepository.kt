package io.moneyflow.server.repository

import io.moneyflow.server.entity.User
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*

interface UserRepository : PagingAndSortingRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
}