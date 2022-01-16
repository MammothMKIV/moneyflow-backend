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
    val date: LocalDate?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_from")
    val accountFrom: Account?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_to")
    val accountTo: Account?,

    @Column(name = "amount")
    val amount: BigDecimal?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    val category: Category?,

    @Column(name = "target_participant")
    val targetParticipant: String?,

    @Column(name = "notes")
    val notes: String?,

    @Column(name = "created_at")
    val createdAt: LocalDateTime?,

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime?,

    @Column(name = "deleted_at")
    val deletedAt: LocalDateTime?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    val createdBy: User?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    val updatedBy: User?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by")
    val deletedBy: User?,
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