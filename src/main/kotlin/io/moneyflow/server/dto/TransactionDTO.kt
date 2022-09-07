package io.moneyflow.server.dto

import io.moneyflow.server.entity.Account
import io.moneyflow.server.entity.Category
import io.moneyflow.server.validation.constraint.ExistingEntity
import io.moneyflow.server.validation.constraint.RequiredAny
import javax.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

@RequiredAny.List([
    RequiredAny(
        message = "accountTo or accountFrom must not be empty",
        fields = ["accountTo", "accountFrom"]
    )
])
data class TransactionDTO(
    var id: Long?,

    @field:NotNull(message = "Date must not be empty")
    var date: LocalDate?,

    @field:ExistingEntity(message = "Invalid account", entityType = Account::class, optional = true)
    var accountFrom: Long?,

    @field:ExistingEntity(message = "Invalid account", entityType = Account::class, optional = true)
    var accountTo: Long?,

    @field:NotNull(message = "Amount must not be empty")
    var amount: BigDecimal?,

    @field:ExistingEntity(message = "Invalid category", entityType = Category::class, optional = true)
    var category: Long?,

    var targetParticipant: String?,

    var notes: String?,
)