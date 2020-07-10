package vn.amit.common.specification

import org.springframework.data.jpa.domain.Specification
import vn.amit.common.enum.CommonStatus
import vn.amit.common.enum.OrderByType
import javax.persistence.criteria.*

fun <T> initSpec(): Specification<T> {
    return Specification { _, _, _ -> null }
}

fun <T> hasStatus(activeStatus: Int = CommonStatus.ACTIVE): Specification<T> {
    return Specification { root, _, criteriaBuilder ->
        criteriaBuilder.equal(root.get<Any>("status"), activeStatus)
    }
}

fun <Root> fetchRelation(relationName: String): Specification<Root> {
    return fetchRelations(listOf(relationName))
}

fun <Root> fetchRelations(relationNames: List<String>): Specification<Root> {
    return Specification { root, query, criteriaBuilder ->
        if (query.resultType != java.lang.Long::class.java && query.resultType != java.lang.Long::class.javaPrimitiveType) {
            query.distinct(true)
            for (relation in relationNames) {
                val relationAsList = relation.split(".")
                var index = 0
                var prevRoot: Fetch<Root, Any>? = null
                do {
                    val r = relationAsList[index]
                    prevRoot = prevRoot?.fetch(r, JoinType.LEFT) ?: root.fetch(r, JoinType.LEFT)
                    index++
                } while (index < relationAsList.size)
            }
        }
        criteriaBuilder.conjunction()
    }
}

fun <Root> distinctRootEntity(): Specification<Root> {
    return Specification { _, query, _ ->
        query.distinct(true)
        null
    }
}

fun <T> orderBy(field: String, orderByType: OrderByType = OrderByType.ASC): Specification<T> {
    return Specification<T> { root, query, criteriaBuilder ->
        val path = root.get<Any>(field)
        query.orderBy(if (orderByType == OrderByType.ASC) {
            criteriaBuilder.asc(path)
        } else {
            criteriaBuilder.desc(path)
        })
        null
    }
}

fun <T> orderBy(orderByOptions: Map<String, OrderByType>): Specification<T> {
    return Specification<T> { root, query, criteriaBuilder ->
        val orders = mutableListOf<Order>()

        orderByOptions.entries.forEach {
            val path = root.get<Any>(it.key)
            orders.add(if (it.value == OrderByType.ASC) {
                criteriaBuilder.asc(path)
            } else {
                criteriaBuilder.desc(path)
            })
        }
        query.orderBy(orders)
        null
    }
}

fun <RootType, RelationType> whereHas(
        relation: String,
        cb: ((relation: Join<RootType, RelationType>, subQuery: Subquery<Long>, criteriaBuilder: CriteriaBuilder) -> Predicate)? = null,
        quantity: Int = 1
): Specification<RootType> {
    return Specification {root, query, criteriaBuilder ->
        whereHasHandler<RootType, RootType, RelationType>(root, query, criteriaBuilder, relation, cb, quantity)
    }
}

/**
 * Make a predicate from a root or a join relation
 *
 * @param root: A Root or a Join relation
 * @param query: A CriteriaQuery or a SubQuery
 * @param criteriaBuilder
 * @param relationName: Name of the relation
 * @param cb: The callback to make addition query on the relation
 * @param quantity: We can check condition for whereHasQuery, if quantity == 1, we check exist, else we check count of query.
 */
fun <HigherRoot, RootType, RelationType>whereHasHandler(
        root: From<HigherRoot, RootType>,
        query: AbstractQuery<*>,
        criteriaBuilder: CriteriaBuilder,
        relationName: String,
        cb: ((relation: Join<RootType, RelationType>, subQuery: Subquery<Long>, criteriaBuilder: CriteriaBuilder) -> Predicate)? = null,
        quantity: Int = 1
): Predicate {
    val subQuery: Subquery<Long> = query.subquery(Long::class.java)

    var join: Join<RootType, RelationType>? = null
    if (root is Root<*>) {
        join = subQuery.correlate(root).join(relationName)
    }
    if (root is Join) {
        val subRoot = subQuery.correlate(root)
        subQuery.select(subRoot.get("id"))
        join = subQuery.correlate(root).join(relationName)
    }
    if (cb != null && join != null) {
        subQuery.where(cb.invoke(join, subQuery, criteriaBuilder))
    }

    return if (quantity == 1) {
        criteriaBuilder.exists(subQuery)
    } else {
        subQuery.select(criteriaBuilder.count(join))
        criteriaBuilder.greaterThanOrEqualTo(subQuery, quantity.toLong())
    }
}