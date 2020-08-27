package vn.amit.common.exception

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.util.WebUtils
import vn.amit.common.language.LocaleService
import vn.amit.common.storage.StorageException
import vn.amit.common.storage.StorageFileNotFoundException
import java.util.*
import javax.validation.ConstraintViolationException
import kotlin.NoSuchElementException

@ControllerAdvice
class CommonExceptionHandler @Autowired constructor(
        private val localeService: LocaleService
): ResponseEntityExceptionHandler()  {

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val field = if (ex.bindingResult.fieldErrors.isNotEmpty()) ex.bindingResult.fieldErrors[0] else null
        val fieldName = field?.let { localeService.getMessage(it.field) } ?: ""
        val message = field?.defaultMessage ?: ex.bindingResult.allErrors.first().defaultMessage
        return buildResponse(message = "$fieldName $message", reTranslate = false, httpStatus = HttpStatus.BAD_REQUEST)
    }

    override fun handleBindException(ex: BindException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val field = if (ex.bindingResult.fieldErrors.size > 0) ex.bindingResult.fieldErrors[0] else null
        val fieldName = field?.let { localeService.getMessage(it.field) } ?: ""
        val message = field?.defaultMessage ?: ex.bindingResult.allErrors.first().defaultMessage
        return buildResponse(message = "$fieldName $message", reTranslate = false, httpStatus = HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(e: NoSuchElementException, req: WebRequest): ResponseEntity<Any> {
        return buildResponse(e.message ?: "failed")
    }

    @ExceptionHandler(EmptyResultDataAccessException::class)
    fun handleEmptyResultDataAccessException(e: EmptyResultDataAccessException): ResponseEntity<Any> {
        return buildResponse("not_found")
    }

    @ExceptionHandler(ExecuteException::class)
    fun handleDefaultException(e: ExecuteException): ResponseEntity<Any> {
        return buildResponse(e.message ?: "failed", 0, e.status, e.messageArgs)
    }

    @ExceptionHandler(StorageException::class)
    fun handleStorageException(e: StorageException): ResponseEntity<Any> {
        return buildResponse(e.message ?: "failed", 0, HttpStatus.BAD_REQUEST, e.messageArgs)
    }

    @ExceptionHandler(StorageFileNotFoundException::class)
    fun handleStorageFileNotFoundException(e: StorageFileNotFoundException): ResponseEntity<Any> {
        return buildResponse(e.message ?: "failed", 0, HttpStatus.NOT_FOUND, e.messageArgs)
    }

    @ExceptionHandler(FileSizeLimitExceededException::class)
    fun handleFileSizeLimitExceededException(e: FileSizeLimitExceededException): ResponseEntity<Any> {
        return buildResponse(e.message ?: "failed", 0, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(e: ConstraintViolationException): ResponseEntity<Any> {
        return buildResponse(e.localizedMessage ?: e.message ?: "failed", 0, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpClientErrorException::class)
    fun handleHttpClientErrorException(e: HttpClientErrorException): ResponseEntity<Any> {
        val objectMapper = ObjectMapper()
        val externalServerErrorResponse = e.responseBodyAsString
        val errorTree = objectMapper.readTree(externalServerErrorResponse)

        val message = when {
            errorTree.has("message") && errorTree.get("message").asText().isNotEmpty() -> {
                localeService.getMessage("error_from_external_service") + ": " + errorTree.get("message").asText()
            }
            errorTree.has("error") -> {
                localeService.getMessage("error_from_external_service") + ": " + errorTree.get("error").asText()
            }
            else -> {
                "failed"
            }
        }
        return buildResponse(message, 0, HttpStatus.BAD_REQUEST)
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
