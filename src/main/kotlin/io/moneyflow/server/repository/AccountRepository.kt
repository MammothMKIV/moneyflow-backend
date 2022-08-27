package io.moneyflow.server.repository

import io.moneyflow.server.entity.Account
import io.moneyflow.server.entity.Household
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

interface AccountRepository : PagingAndSortingRepository<Account, Long> {
    fun findAllByHouseholdIn(households: List<Household>, pageable: Pageable): Page<Account>
}