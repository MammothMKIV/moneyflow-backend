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
    val id: Long?,

    @Column(name = "type")
    var type: OperationConfirmationType,

    @Column(name = "series")
    var series: String,

    @Column(name = "token")
    var token: String,

    @Column(name = "expiring_at")
    var expiringAt: LocalDateTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User?,

    @Column(name = "target_id")
    var targetId: String?,

    @Column(name = "created_at")
    var createdAt: LocalDateTime,

    @Column(name = "additional_data")
    var additionalData: String?,
)