package vn.amit.common.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

interface RestResponseService {
    fun restSuccess(data: Any? = null, meta: Any? = null, message: String = "success", status: Int = 1): ResponseEntity<Any>
    fun restError(message: String = "failed", status: HttpStatus = HttpStatus.NOT_FOUND, messageArgs: Array<Any>? = null): ResponseEntity<Any>
    fun restException(e: Exception): ResponseEntity<Any>
}