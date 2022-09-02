package io.moneyflow.server.service

import io.moneyflow.server.entity.Household
import io.moneyflow.server.entity.NamePlaceholder
import io.moneyflow.server.repository.NamePlaceholderRepository
import org.springframework.stereotype.Service

@Service
class NamePlaceholderService(
    val namePlaceholderRepository: NamePlaceholderRepository
) {
    fun getByHousehold(household: Household): List<NamePlaceholder> {
        return namePlaceholderRepository.findByHousehold(household)
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