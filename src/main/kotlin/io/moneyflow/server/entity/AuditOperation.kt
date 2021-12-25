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
@Table(name = "audit_operations")
class AuditOperation(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    @Column(name = "performed_at")
    val performedAt: LocalDateTime,

    @Column(name = "target_id")
    val targetId: String,

    @Column(name = "type")
    val type: AuditOperationType,

    @Column(name = "payload")
    val payload: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    val user: User,

    @Column(name = "target_type")
    val targetType: AuditOperationTargetType,
)