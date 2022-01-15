package io.moneyflow.server.service

import io.moneyflow.server.entity.Household
import io.moneyflow.server.entity.Transaction
import io.moneyflow.server.repository.HouseholdRepository
import io.moneyflow.server.repository.TransactionRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class HouseholdService(
    val householdRepository: HouseholdRepository
) {
    fun getAll(page: Int, perPage: Int): Page<Household> {
        return householdRepository.findAll(PageRequest.of(page, perPage, Sort.by("createdAt").descending()))
    }

    fun save(household: Household) {
        householdRepository.save(household)
    }

    fun get(id: Long): Household? {
        return householdRepository.findById(id).orElse(null)
    }

    fun delete(id: Long) {
        householdRepository.deleteById(id)
    }
}