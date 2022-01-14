package io.moneyflow.server.dto
import com.sun.istack.NotNull
import java.math.BigDecimal
import java.time.LocalDate
import javax.validation.constraints.Null

data class TransactionDTO(
    @Null
    var id: Long?,

    @NotNull
    var date: LocalDate?,

    var accountFrom: Long?,

    var accountTo: Long?,

    @NotNull
    var amount: BigDecimal?,

    var category: Long?,

    var targetParticipant: String?,

    var notes: String?,
)