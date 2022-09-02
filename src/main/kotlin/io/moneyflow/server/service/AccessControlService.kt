package io.moneyflow.server.service

import io.moneyflow.server.entity.Household
import io.moneyflow.server.entity.Transaction
import io.moneyflow.server.entity.User
import org.springframework.stereotype.Service

@Service
class AccessControlService {
    fun canManageHousehold(user: User, household: Household): Boolean {
        return household.owner == user
    }

    fun canManageTransactionHouseholds(user: User, transaction: Transaction): Boolean {
        return (transaction.accountFrom != null && !canManageHousehold(user, transaction.accountFrom!!.household))
            || (transaction.accountTo != null && !canManageHousehold(user, transaction.accountTo!!.household))
    }
}