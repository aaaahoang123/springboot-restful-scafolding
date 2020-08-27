package vn.amit.common.storage

open class StorageException(message: String, cause: Throwable? = null, val messageArgs: Array<Any>? = null) : RuntimeException(message, cause)