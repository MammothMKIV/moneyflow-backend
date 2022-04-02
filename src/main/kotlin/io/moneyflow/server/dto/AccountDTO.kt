package io.moneyflow.server.dto

import javax.validation.constraints.NotNull
import io.moneyflow.server.entity.AccountType
import java.math.BigDecimal
import javax.validation.constraints.NotBlank

data class AccountDTO(
    var id: Long?,

    @field:NotBlank(message = "Name must not be empty")
    var name: String,

    @field:NotNull(message = "Initial balance must not be empty")
    var initialBalance: BigDecimal,

    @field:NotNull(message = "Account type must not be empty")
    var type: AccountType,
)