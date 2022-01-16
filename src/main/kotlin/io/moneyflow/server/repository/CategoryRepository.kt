package io.moneyflow.server.repository

import io.moneyflow.server.entity.Category
import org.springframework.data.repository.PagingAndSortingRepository

interface CategoryRepository : PagingAndSortingRepository<Category, Long> {

}