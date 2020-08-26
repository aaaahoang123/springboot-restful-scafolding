package vn.amit.common.constraint

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import vn.amit.common.constraint.contract.ValidateClassConstrainValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.full.memberProperties

@Component
@Scope("request")
class NotNullIfFieldValidator : ValidateClassConstrainValidator<NotNullIfField, Any, Any>() {
    private lateinit var conditionField: String
    private lateinit var operator: NotNullIfFieldOperator
    private lateinit var value: String
    override fun initialize(constraintAnnotation: NotNullIfField) {
        conditionField = constraintAnnotation.whenField
        operator = constraintAnnotation.operator
        value = constraintAnnotation.value
    }

    override fun isValid(fieldValue: Any?, context: ConstraintValidatorContext): Boolean {
        if (instance == null || fieldValue != null) return true
        val valueToCheck = instance!!::class.memberProperties.find { it.name == conditionField }?.getter?.call(instance)

        val isNotValid = (
                (operator == NotNullIfFieldOperator.EQUALS && valueToCheck?.toString() == value)
                        || (operator == NotNullIfFieldOperator.IS_NULL && valueToCheck == null)
                        || (operator == NotNullIfFieldOperator.NOT_EQUALS && valueToCheck?.toString() != value)
                        || (operator == NotNullIfFieldOperator.NOT_NULL && valueToCheck != null)
                )
        return !isNotValid
    }

    override fun preparedParamsForMessage(context: HibernateConstraintValidatorContext): HibernateConstraintValidatorContext {
        return context
                .addMessageParameter("fields", fieldName)
                .addMessageParameter("validatedBy", conditionField)
                .addMessageParameter("operator", operator.name.toLowerCase())
                .addMessageParameter("value", value)
    }
}