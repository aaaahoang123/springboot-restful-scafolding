package vn.amit.common.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import vn.amit.common.language.LocaleService
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
class RestResponseServiceImpl @Autowired constructor(
        private val localeService: LocaleService
): RestResponseService {
    override fun restSuccess(data: Any?, meta: Any?, message: String, status: Int): ResponseEntity<Any> {
        val res = mutableMapOf<String, Any?>(
                "status" to status,
                "message" to localeService.getMessage(message)
        )
        if (data?.javaClass?.isArray == true || data?.javaClass?.let { it -> List::class.java.isAssignableFrom(it)} == true) {
            res["datas"] = data
        } else {
            res["data"] = data
        }
        if (meta != null) {
            res["meta"] = meta
        }
        return ResponseEntity(res, HttpStatus.OK)
    }

    override fun restError(message: String, status: HttpStatus, messageArgs: Array<Any>?): ResponseEntity<Any> {
        return ResponseEntity(mapOf(
                "timestamp" to Calendar.getInstance(),
                "status" to status.value(),
                "message" to localeService.getMessage(message, messageArgs)
        ), status)
    }

    override fun restException(e: Exception): ResponseEntity<Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}