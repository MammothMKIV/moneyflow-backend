package io.moneyflow.server.service

import io.moneyflow.server.entity.NamePlaceholder
import io.moneyflow.server.repository.NamePlaceholderRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class NamePlaceholderService(
    val namePlaceholderRepository: NamePlaceholderRepository
) {
    fun getAll(page: Int, perPage: Int): Page<NamePlaceholder> {
        return namePlaceholderRepository.findAll(PageRequest.of(page, perPage, Sort.by("createdAt").descending()))
    }

    fun save(namePlaceholder: NamePlaceholder) {
        namePlaceholderRepository.save(namePlaceholder)
    }

    fun get(id: Long): NamePlaceholder? {
        return namePlaceholderRepository.findById(id).orElse(null)
    }

    fun delete(id: Long) {
        namePlaceholderRepository.deleteById(id)
    }
}