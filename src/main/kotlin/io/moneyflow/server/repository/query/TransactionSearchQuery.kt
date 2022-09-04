package io.moneyflow.server.repository.query

import io.moneyflow.server.entity.Account
import io.moneyflow.server.entity.Category
import io.moneyflow.server.entity.Household
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class TransactionSearchQuery(
    val page: Int?,
    val perPage: Int?,
    val accountFrom: Account?,
    val accountTo: Account?,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val dateFrom: LocalDate?,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val dateTo: LocalDate?,
    val category: Category?,
    val household: Household?,
)
