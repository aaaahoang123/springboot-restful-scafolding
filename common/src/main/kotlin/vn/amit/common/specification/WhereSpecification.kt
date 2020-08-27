package vn.amit.common.specification

import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.*

class WhereSpecification<T>(
        private val column: String,
        private val operator: WhereOperator,
        private val value: Any
) : Specification<T> {
    override fun toPredicate(root: Root<T>, query: CriteriaQuery<*>, cb: CriteriaBuilder): Predicate? {
        val expressionAny = root.get<Any>(column)
        val expression = root.get<String>(column)
        return when(operator) {
            WhereOperator.EQUALS -> cb.equal(expressionAny, value)
            WhereOperator.END -> cb.like(expression, "%$value")
            WhereOperator.START -> cb.like(expression, "$value%")
            WhereOperator.LIKE -> cb.like(expression, "%$value%")
            WhereOperator.CUSTOM_LIKE -> cb.like(expression, value as String)
            WhereOperator.GT -> cb.greaterThan(expression, value.toString())
            WhereOperator.GTE -> cb.greaterThanOrEqualTo(expression, value.toString())
            WhereOperator.LT -> cb.lessThan(expression, value.toString())
            WhereOperator.LTE -> cb.lessThanOrEqualTo(expression, value.toString())
        }
    }
}