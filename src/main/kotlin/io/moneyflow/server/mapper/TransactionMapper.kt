package io.moneyflow.server.mapper

import io.moneyflow.server.dto.TransactionDTO
import io.moneyflow.server.entity.Transaction
import io.moneyflow.server.entity.Account
import io.moneyflow.server.entity.Category
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import java.util.Optional

@Mapper(
    imports = [
        Account::class,
        Category::class,
        Optional::class,
    ],
    componentModel = "spring",
    uses = [
        AccountMapper::class,
        CategoryMapper::class
    ]
)
interface TransactionMapper {
    fun map(transaction: Transaction): TransactionDTO

    @Mappings(
        value = [
            Mapping(source = "accountFrom", target = "accountFrom"),
            Mapping(source = "accountTo", target = "accountTo"),
            Mapping(source = "category", target = "category"),
        ]
    )
    fun map(transactionDTO: TransactionDTO): Transaction

    fun accountId(account: Account?): Long? {
        return account?.id
    }

    fun categoryId(category: Category?): Long? {
        return category?.id
    }
}