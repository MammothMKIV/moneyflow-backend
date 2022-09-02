package io.moneyflow.server.service

import io.moneyflow.server.entity.Category
import io.moneyflow.server.entity.Household
import io.moneyflow.server.repository.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryService(
    val categoryRepository: CategoryRepository
) {
    fun getByHousehold(household: Household): List<Category> {
        return categoryRepository.findByHousehold(household)
    }

    fun save(category: Category) {
        categoryRepository.save(category)
    }

    fun get(id: Long): Category? {
        return categoryRepository.findById(id).orElse(null)
    }

    fun delete(id: Long) {
        categoryRepository.deleteById(id)
    }
}