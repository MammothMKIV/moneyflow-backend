package io.moneyflow.server.validation.validator

import io.moneyflow.server.validation.constraint.RequiredAny
import java.lang.RuntimeException
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.full.memberProperties

class RequiredAnyValidator(
    private var constraintAnnotation: RequiredAny?
) : ConstraintValidator<RequiredAny, Any> {
    override fun initialize(constraintAnnotation: RequiredAny?) {
        super.initialize(constraintAnnotation)
        this.constraintAnnotation = constraintAnnotation
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        if (constraintAnnotation == null || value == null) {
            return false
        }

        var hasNonNullField = false

        constraintAnnotation!!.fields.forEach {
            val fieldValue = value::class.memberProperties.stream()
                .filter { member -> member.name == it }
                .findAny()
                .orElseThrow { RuntimeException("Field '" + it + "' not found on " + value.javaClass.name) }
                .getter
                .call(value)

            if (fieldValue != null) {
                hasNonNullField = true
                return@forEach
            }
        }

        return hasNonNullField
    }
}