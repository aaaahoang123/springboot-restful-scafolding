package vn.amit.common.constraint

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMembers

class ValueOfEnumValidator : ConstraintValidator<ValueOfEnum, Any> {
    private lateinit var enumerates: KClass<*>

    override fun initialize(constraintAnnotation: ValueOfEnum) {
        enumerates = constraintAnnotation.enumerates
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        return value == null
                || enumerates.declaredMembers
                .map { it.call() }
                .contains(value)
    }
}