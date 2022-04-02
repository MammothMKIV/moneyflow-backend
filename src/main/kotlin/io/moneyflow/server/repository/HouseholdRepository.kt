package io.moneyflow.server.repository

import io.moneyflow.server.entity.Household
import io.moneyflow.server.entity.Transaction
import io.moneyflow.server.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

interface HouseholdRepository : PagingAndSortingRepository<Household, Long> {
    fun findAllByOwner(owner: User, pageable: Pageable): Page<Household>
}