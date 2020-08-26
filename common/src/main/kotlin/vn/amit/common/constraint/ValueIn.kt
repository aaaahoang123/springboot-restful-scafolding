package vn.amit.common.constraint

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(
        AnnotationTarget.PROPERTY,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.FIELD,
        AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.CONSTRUCTOR,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.TYPE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [ValueInValidator::class])
annotation class ValueIn(
        val anyOf: Array<String> = [],
        val message: String = "{vn.amit.common.constraint.ValueIn.message}",
        val groups: Array<KClass<out Any>> = [],
        val payload: Array<KClass<out Payload>> = []
)