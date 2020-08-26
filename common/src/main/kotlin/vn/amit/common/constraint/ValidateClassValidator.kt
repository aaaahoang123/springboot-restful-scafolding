package vn.amit.common.constraint

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import vn.amit.common.constraint.contract.ValidateClassConstrainValidator
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

class ValidateClassValidator @Autowired constructor(
        private val app: ApplicationContext
) : ConstraintValidator<ValidateClass, Any> {
    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return true

        value::class.memberProperties.forEach {
            it.javaField?.annotations?.forEach { anno ->
                val validators = anno.annotationClass.findAnnotation<Constraint>()
                if (
                        anno.annotationClass.findAnnotation<ValidateClass>() != null
                        && validators != null
                        && validators.validatedBy.all { validator -> validator.isSubclassOf(ValidateClassConstrainValidator::class) }
                ) {
                    for (validator in validators.validatedBy) {
                        val validatorInstance = app.getBean(validator.java) as ValidateClassConstrainValidator<in Annotation, Any, Any>
                        validatorInstance.initialize(anno)
                        validatorInstance.prepareInstance(value, it.name)
                        if (!validatorInstance.isValid(it.getter.call(value), context)) {
                            val hibernateContext = context.unwrap(HibernateConstraintValidatorContext::class.java)
                            hibernateContext.disableDefaultConstraintViolation()
                            validatorInstance.preparedParamsForMessage(hibernateContext)
                                    .buildConstraintViolationWithTemplate(anno::class.memberProperties
                                            .find { prop -> prop.name == "message" }
                                            ?.getter?.call(anno) as String? ?: ""
                                    )
                                    .addConstraintViolation()

                            return false
                        }

                    }
                }
            }
        }

        return true
    }
}