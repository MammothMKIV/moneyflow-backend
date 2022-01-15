package io.moneyflow.server.repository

import io.moneyflow.server.entity.Household
import io.moneyflow.server.entity.Transaction
import org.springframework.data.repository.PagingAndSortingRepository

interface HouseholdRepository : PagingAndSortingRepository<Household, Long> {

}