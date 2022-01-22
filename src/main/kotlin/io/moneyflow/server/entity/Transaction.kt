package io.moneyflow.server.entity

import io.moneyflow.server.mapper.Default
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "transactions")
class Transaction @Default constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long?,

    @Column(name = "date")
    var date: LocalDate?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_from")
    var accountFrom: Account?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_to")
    var accountTo: Account?,

    @Column(name = "amount")
    var amount: BigDecimal?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    var category: Category?,

    @Column(name = "target_participant")
    var targetParticipant: String?,

    @Column(name = "notes")
    var notes: String?,

    @Column(name = "created_at")
    var createdAt: LocalDateTime?,

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime?,

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    var createdBy: User?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    var updatedBy: User?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by")
    var deletedBy: User?,
) {
    constructor(date: LocalDate, amount: BigDecimal) : this(
        null,
        date,
        null,
        null,
        amount,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
}