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
@Table(name = "operation_confirmations")
class OperationConfirmation(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    @Column(name = "type")
    val type: OperationConfirmationType,

    @Column(name = "nonce")
    val nonce: String,

    @Column(name = "expiring_at")
    val expiringAt: LocalDateTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    val user: User?,

    @Column(name = "target_id")
    val targetId: String?,

    @Column(name = "created_at")
    val createdAt: LocalDateTime,

    @Column(name = "additional_data")
    val additionalData: String?,
)