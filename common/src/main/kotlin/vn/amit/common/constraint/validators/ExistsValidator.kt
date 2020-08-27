package vn.amit.common.constraint.validators

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import vn.amit.common.constraint.Exists
import vn.amit.common.repository.BaseRepository
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class ExistsValidator : ConstraintValidator<Exists, Any> {
    lateinit var column: String
    lateinit var repository: BaseRepository<out Any, out Any>

    @Autowired
    lateinit var applicationContext: ApplicationContext

    override fun initialize(constraintAnnotation: Exists) {
        column = constraintAnnotation.column
        repository = applicationContext.getBean(constraintAnnotation.repository.java)
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        return value?.let {
            repository.count { root, _, cb ->  cb.equal(root.get<Any>(column), it)} != 0L
        } ?: true
    }

}