package io.moneyflow.server.entity

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
@Table(name = "accounts")
class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "household")
    var household: Household,

    @Column(name = "created_at")
    var createdAt: LocalDateTime?,

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime?,

    @Column(name = "initial_balance")
    var initialBalance: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    var createdBy: User?,

    @Column(name = "name")
    var name: String,

    @Column(name = "type")
    var type: AccountType,

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by")
    var deletedBy: User?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    var updatedBy: User?,
)