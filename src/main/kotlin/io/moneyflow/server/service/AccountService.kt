package io.moneyflow.server.service

import io.moneyflow.server.entity.Account
import io.moneyflow.server.entity.Household
import io.moneyflow.server.repository.AccountRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class AccountService(
    val accountRepository: AccountRepository
) {
    fun getAllByHouseholds(page: Int, perPage: Int, households: List<Household>): Page<Account> {
        return accountRepository.findAllByHouseholdIn(households, PageRequest.of(page, perPage, Sort.by("createdAt").descending()))
    }

    fun save(account: Account) {
        accountRepository.save(account)
    }

    fun get(id: Long): Account? {
        return accountRepository.findById(id).orElse(null)
    }

    fun delete(id: Long) {
        accountRepository.deleteById(id)
    }
}