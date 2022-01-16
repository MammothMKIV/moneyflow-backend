package io.moneyflow.server.repository

import io.moneyflow.server.entity.Account
import org.springframework.data.repository.PagingAndSortingRepository

interface AccountRepository : PagingAndSortingRepository<Account, Long> {

}