package io.moneyflow.server.service

import io.moneyflow.server.entity.Account
import io.moneyflow.server.entity.Category
import io.moneyflow.server.entity.Household
import io.moneyflow.server.repository.query.TransactionSearchQuery
import io.moneyflow.server.entity.Transaction
import io.moneyflow.server.entity.User
import io.moneyflow.server.repository.TransactionRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Service
class TransactionService(
    val transactionRepository: TransactionRepository,
    val householdService: HouseholdService,
) {
    fun search(query: TransactionSearchQuery, user: User): Page<Transaction> {
        val page = query.page ?: 0
        val perPage = query.perPage ?: 20
        val pageable = PageRequest.ofSize(perPage).withPage(page).withSort(Sort.by("date").descending().and(Sort.by("id").descending()))
        val userHouseholds = householdService.getAllOwnedBy(user)

        if (userHouseholds.isEmpty()) {
            return Page.empty()
        }

        return transactionRepository.findAll({root, q, builder ->
            val predicates = ArrayList<Predicate>()

            if (query.accountFrom != null) {
                predicates.add(builder.equal(root.get<Account>("accountFrom"), query.accountFrom))
            }

            if (query.accountTo != null) {
                predicates.add(builder.equal(root.get<Account>("accountTo"), query.accountTo))
            }

            if (query.category != null) {
                predicates.add(builder.equal(root.get<Category>("category"), query.category))
            }

            if (query.dateTo != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("date"), query.dateTo))
            }

            if (query.dateFrom != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("date"), query.dateFrom))
            }

            root.join<Transaction, Account>("accountFrom", JoinType.LEFT)
            root.join<Transaction, Account>("accountTo", JoinType.LEFT)

            if (query.household != null) {
                predicates.add(
                    builder.or(
                        builder.equal(root.get<Account>("accountFrom").get<Household>("household"), query.household),
                        builder.equal(root.get<Account>("accountTo").get<Household>("household"), query.household),
                    )
                )
            }

            predicates.add(
                builder.or(
                    root.get<Account>("accountFrom").get<Household>("household").`in`(userHouseholds),
                    root.get<Account>("accountTo").get<Household>("household").`in`(userHouseholds),
                )
            )

            builder.and(*predicates.toTypedArray())
        }, pageable)
    }

    fun save(transaction: Transaction) {
        transactionRepository.save(transaction)
    }

    fun get(id: Long): Transaction? {
        return transactionRepository.findById(id).orElse(null)
    }

    fun delete(id: Long) {
        transactionRepository.deleteById(id)
    }
}