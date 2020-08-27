package vn.amit.common.constraint

import vn.amit.common.constraint.contract.NotNullIfFieldOperator
import vn.amit.common.constraint.validators.NotNullIfFieldValidator
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
@ValidateClass
@MustBeDocumented
@Constraint(validatedBy = [NotNullIfFieldValidator::class])
annotation class NotNullIfField(
        val whenField: String,
        val operator: NotNullIfFieldOperator = NotNullIfFieldOperator.NOT_NULL,
        val value: String = "",
        val message: String = "{vn.amit.common.constraint.NotNullIfField.message}",
        val groups: Array<KClass<out Any>> = [],
        val payload: Array<KClass<out Payload>> = []
)