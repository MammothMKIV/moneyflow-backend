package io.moneyflow.server.dto
import com.sun.istack.NotNull
import java.math.BigDecimal
import java.time.LocalDate
import javax.validation.constraints.Null

data class HouseholdDTO(
    @Null
    var id: Long?,

    @NotNull
    var name: String,
)