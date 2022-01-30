package io.moneyflow.server.repository

import io.moneyflow.server.entity.OperationConfirmation
import io.moneyflow.server.entity.OperationConfirmationType
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*

interface OperationConfirmationRepository : PagingAndSortingRepository<OperationConfirmation, Long> {
    fun findBySeriesAndType(series: String, type: OperationConfirmationType): Optional<OperationConfirmation>
}