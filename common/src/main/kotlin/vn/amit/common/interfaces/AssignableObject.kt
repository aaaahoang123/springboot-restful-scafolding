package vn.amit.common.interfaces

interface AssignableObject<T> {
    fun afterInitialized(instance: T)
}