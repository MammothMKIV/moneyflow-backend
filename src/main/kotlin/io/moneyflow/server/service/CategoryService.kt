package io.moneyflow.server.service

import io.moneyflow.server.entity.Category
import io.moneyflow.server.repository.CategoryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class CategoryService(
    val categoryRepository: CategoryRepository
) {
    fun getAll(page: Int, perPage: Int): Page<Category> {
        return categoryRepository.findAll(PageRequest.of(page, perPage, Sort.by("createdAt").descending()))
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