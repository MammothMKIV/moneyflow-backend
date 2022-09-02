package io.moneyflow.server.repository

import io.moneyflow.server.entity.Household
import io.moneyflow.server.entity.NamePlaceholder
import org.springframework.data.repository.PagingAndSortingRepository

interface NamePlaceholderRepository : PagingAndSortingRepository<NamePlaceholder, Long> {
    fun findByHousehold(household: Household): List<NamePlaceholder>
}