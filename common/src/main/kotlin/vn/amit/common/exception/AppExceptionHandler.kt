package vn.amit.common.exception

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
//import org.springframework.security.authentication.BadCredentialsException
//import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.util.WebUtils
import vn.amit.common.language.LocaleService
import java.util.*
import kotlin.NoSuchElementException

@ControllerAdvice
class AppExceptionHandler @Autowired constructor(
        private val localeService: LocaleService
): ResponseEntityExceptionHandler()  {

//    @ExceptionHandler(value = [
//        UsernameNotFoundException::class,
//        BadCredentialsException::class
//    ])
//    fun handleAuthException(e: UsernameNotFoundException): ResponseEntity<Any> {
//        return buildResponse(e.message ?: "failed", 0, HttpStatus.UNAUTHORIZED)
//    }

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val field = ex.bindingResult.fieldErrors[0]
        val fieldName = localeService.getMessage(field.field)
        val message = field.defaultMessage
        return buildResponse(message = "$fieldName $message", reTranslate = false)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(e: NoSuchElementException): ResponseEntity<Any> {
        return buildResponse(e.message ?: "failed")
    }

    @ExceptionHandler(ExecuteException::class)
    fun handleDefaultException(e: ExecuteException): ResponseEntity<Any> {
        return buildResponse(e.message ?: "failed", 0, e.status, e.messageArgs)
    }

    override fun handleExceptionInternal(ex: Exception, body: Any?, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        if (HttpStatus.INTERNAL_SERVER_ERROR == status) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST)
        }
        val message =
                when {
                    body is String -> body
                    ex.message != null -> ex.message!!
                    else -> "failed"
                }
        return buildResponse(message, 0, status)
    }

    fun buildResponse(message: String = "failed", customStatus: Int? = null, httpStatus: HttpStatus = HttpStatus.NOT_FOUND, messageArgs: Array<Any>? = null, reTranslate: Boolean = true): ResponseEntity<Any> {
        return ResponseEntity(mapOf(
                "timestampMs" to Calendar.getInstance().timeInMillis,
                "status" to (customStatus ?: 0),
                "message" to if (reTranslate) localeService.getMessage(message, messageArgs) else message
        ), httpStatus)
    }
}
