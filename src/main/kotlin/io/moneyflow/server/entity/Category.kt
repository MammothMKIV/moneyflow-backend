package io.moneyflow.server.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.moneyflow.server.mapper.Default
import io.moneyflow.server.serialization.EntityIdResolver
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
@Table(name = "categories")
class Category @Default constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long?,

    @Column(name = "name")
    val name: String,

    @Column(name = "icon")
    val icon: String,

    @Column(name = "color")
    val color: String,

    @Column(name = "type")
    val type: CategoryType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "household")
    @JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator::class,
        property = "id",
        resolver = EntityIdResolver::class,
        scope = Household::class
    )
    @JsonIdentityReference(alwaysAsId = true)
    val household: Household,

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
)