package vn.amit.common.constraint

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class ValueInValidator : ConstraintValidator<ValueIn, Any> {
    private lateinit var anyOfStr: Array<String>

    override fun initialize(constraintAnnotation: ValueIn) {
        anyOfStr = constraintAnnotation.anyOf
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        return value == null
                || (anyOfStr.contains(value.toString()))

    }

}