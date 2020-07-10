package vn.amit.common.storage

class StorageFileNotFoundException(message: String, cause: Throwable? = null, messageArgs: Array<Any>? = null)
    : StorageException(message, cause, messageArgs)