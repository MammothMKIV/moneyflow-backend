package io.moneyflow.server.service

import io.moneyflow.server.entity.Transaction
import io.moneyflow.server.repository.TransactionRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class TransactionService(
    val transactionRepository: TransactionRepository
) {
    fun getAll(page: Int, perPage: Int): Page<Transaction> {
        return transactionRepository.findAll(PageRequest.of(page, perPage, Sort.by("createdAt").descending()))
    }

    fun save(transaction: Transaction) {
        transactionRepository.save(transaction)
    }

    fun get(id: Long): Transaction? {
        return transactionRepository.findById(id).orElse(null)
    }

    fun delete(id: Long) {
        transactionRepository.deleteById(id)
    }
}