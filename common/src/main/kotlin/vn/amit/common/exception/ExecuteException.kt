package vn.amit.common.exception

import org.springframework.http.HttpStatus

class ExecuteException(
        message: String?,
        val status: HttpStatus = HttpStatus.NOT_FOUND,
        val messageArgs: Array<Any>? = null
): Exception(message)