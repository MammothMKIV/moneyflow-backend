package io.moneyflow.server.mapper

import io.moneyflow.server.dto.TransactionDTO
import io.moneyflow.server.entity.Transaction
import io.moneyflow.server.entity.Account
import io.moneyflow.server.entity.Category
import org.mapstruct.Mapper

@Mapper(
    componentModel = "spring",
    uses = [
        AccountMapper::class,
        CategoryMapper::class
    ]
)
interface TransactionMapper {
    fun map(transaction: Transaction): TransactionDTO

    fun map(transactionDTO: TransactionDTO): Transaction

    fun accountId(account: Account?): Long? {
        return account?.id
    }

    fun categoryId(category: Category?): Long? {
        return category?.id
    }
}