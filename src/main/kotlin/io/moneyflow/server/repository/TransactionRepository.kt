package io.moneyflow.server.repository

import io.moneyflow.server.entity.Transaction
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : PagingAndSortingRepository<Transaction, Long>,
    JpaSpecificationExecutor<Transaction> {

}