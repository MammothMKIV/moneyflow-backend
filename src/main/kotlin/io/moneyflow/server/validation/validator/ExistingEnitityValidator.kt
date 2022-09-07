package io.moneyflow.server.validation.validator

import io.moneyflow.server.validation.constraint.ExistingEntity
import javax.persistence.EntityManager
import javax.persistence.EntityNotFoundException
import javax.persistence.PersistenceContext
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class ExistingEntityValidator(
    private var constraintAnnotation: ExistingEntity?,
    @PersistenceContext
    val entityManager: EntityManager
) : ConstraintValidator<ExistingEntity, Any> {
    override fun initialize(constraintAnnotation: ExistingEntity?) {
        super.initialize(constraintAnnotation)
        this.constraintAnnotation = constraintAnnotation
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        if (constraintAnnotation == null) {
            return false
        }

        if (value == null && constraintAnnotation!!.optional) {
            return true
        }

        return try {
            if (value == null) false else entityManager.getReference(constraintAnnotation!!.entityType.java, value) != null
        } catch (e: EntityNotFoundException) {
            false
        }
    }
}