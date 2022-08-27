package io.moneyflow.server.dto

import javax.validation.constraints.NotNull
import io.moneyflow.server.entity.AccountType
import io.moneyflow.server.entity.Household
import io.moneyflow.server.validation.constraint.ExistingEntity
import java.math.BigDecimal
import javax.validation.constraints.NotBlank

data class AccountDTO(
    var id: Long?,

    @field:NotNull(message = "Name must not be empty")
    @field:NotBlank(message = "Name must not be empty")
    var name: String,

    @field:NotNull(message = "Initial balance must not be empty")
    var initialBalance: BigDecimal,

    @field:NotNull(message = "Account type must not be empty")
    var type: AccountType,

    @field:NotNull(message = "Household must not be empty")
    @field:ExistingEntity(message = "Invalid household", entityType = Household::class)
    var household: Long,
)