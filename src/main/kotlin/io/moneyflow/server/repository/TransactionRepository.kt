package io.moneyflow.server.repository

import io.moneyflow.server.entity.Transaction
import org.springframework.data.repository.PagingAndSortingRepository

interface TransactionRepository : PagingAndSortingRepository<Transaction, Long> {

}