package vn.amit.common.constraint.validators

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import vn.amit.common.constraint.Unique
import vn.amit.common.repository.BaseRepository
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class UniqueValidator : ConstraintValidator<Unique, Any> {
    lateinit var column: String
    lateinit var repository: BaseRepository<out Any, out Any>

    @Autowired
    lateinit var applicationContext: ApplicationContext

    override fun initialize(constraintAnnotation: Unique) {
        column = constraintAnnotation.column
        repository = applicationContext.getBean(constraintAnnotation.repository.java)
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        return value?.let {
            val col = if (column.isNotEmpty()) {
                column
            } else {
                // Get the name of the property as the column name to check
                (context as ConstraintValidatorContextImpl).constraintViolationCreationContexts[0].path.leafNode.name
            }
            repository.count { root, _, cb ->  cb.equal(root.get<Any>(col), it)} == 0L
        } ?: true
    }

}