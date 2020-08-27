package vn.amit.common.constraint.contract

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import javax.validation.ConstraintValidator

abstract class ValidateClassConstrainValidator<AnnotationType : Annotation?, ValueType, ClassType> : ConstraintValidator<AnnotationType, ValueType> {
    protected var instance: ClassType? = null
    protected var fieldName: String? = null

    open fun prepareInstance(instance: ClassType, fieldName: String) {
        this.instance = instance
        this.fieldName = fieldName
    }

    open fun preparedParamsForMessage(context: HibernateConstraintValidatorContext): HibernateConstraintValidatorContext {
        return context
    }
}