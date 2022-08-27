package io.moneyflow.server.service

import io.moneyflow.server.entity.Account
import io.moneyflow.server.entity.Household
import io.moneyflow.server.entity.User
import org.springframework.stereotype.Service

@Service
class AccessControlService {
    fun canCreateAccountsInHousehold(user: User, household: Household): Boolean {
        return household.owner == user
    }

    fun canEditAccount(user: User, account: Account): Boolean {
        return account.household.owner == user
    }

    fun canDeleteAccount(user: User, account: Account): Boolean {
        return account.household.owner == user
    }
}