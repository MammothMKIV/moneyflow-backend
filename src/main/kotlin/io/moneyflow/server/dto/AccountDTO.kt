package io.moneyflow.server.dto
import com.sun.istack.NotNull
import io.moneyflow.server.entity.AccountType
import java.math.BigDecimal
import javax.validation.constraints.Null

data class AccountDTO(
    @Null
    var id: Long?,

    @NotNull
    var name: String,

    @NotNull
    var initialBalance: BigDecimal,

    @NotNull
    var type: AccountType,
)