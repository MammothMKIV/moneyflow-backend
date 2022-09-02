package io.moneyflow.server.repository

import io.moneyflow.server.entity.Category
import io.moneyflow.server.entity.Household
import org.springframework.data.repository.PagingAndSortingRepository

interface CategoryRepository : PagingAndSortingRepository<Category, Long> {
    fun findByHousehold(household: Household): List<Category>
}