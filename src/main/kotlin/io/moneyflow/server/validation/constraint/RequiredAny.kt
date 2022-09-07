package io.moneyflow.server.validation.constraint

import io.moneyflow.server.validation.validator.RequiredAnyValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [RequiredAnyValidator::class])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequiredAny(
    val message: String,
    val fields: Array<String>,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
) {
    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class List(
        val value: Array<RequiredAny>
    )
}
