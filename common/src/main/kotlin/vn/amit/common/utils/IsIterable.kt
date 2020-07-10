package vn.amit.common.utils

fun isIterable(target: Any?): Boolean {
    return target?.javaClass?.isArray == true || target?.javaClass?.let { List::class.java.isAssignableFrom(it) } == true
}