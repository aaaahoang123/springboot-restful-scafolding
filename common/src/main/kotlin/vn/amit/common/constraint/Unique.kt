package vn.amit.common.constraint

import vn.amit.common.constraint.validators.UniqueValidator
import vn.amit.common.repository.BaseRepository
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
@Constraint(validatedBy = [UniqueValidator::class])
annotation class Unique(
        val repository: KClass<out BaseRepository<out Any, out Any>>,
        val column: String = "",
        val message: String = "{vn.amit.common.constraint.Unique.message}",
        val groups: Array<KClass<out Any>> = [],
        val payload: Array<KClass<out Payload>> = []
)