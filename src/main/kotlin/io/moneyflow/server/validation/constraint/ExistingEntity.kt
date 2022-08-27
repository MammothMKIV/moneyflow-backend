package io.moneyflow.server.validation.constraint

import io.moneyflow.server.validation.validator.ExistingEntityValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [ExistingEntityValidator::class])
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExistingEntity(
    val message: String,
    val entityType: KClass<*>,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
