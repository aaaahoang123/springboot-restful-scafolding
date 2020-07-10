package vn.amit.common.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import vn.amit.common.language.LocaleService
import vn.amit.common.utils.isIterable
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class RestfulResponseBodyAdvice @Autowired constructor(
        private val localeService: LocaleService,
        private val req: HttpServletRequest
) : ResponseBodyAdvice<Any?> {
    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return !ResponseEntity::class.java.isAssignableFrom(returnType.method?.returnType!!)
                && req.getHeader(HttpHeaders.ACCEPT) == "application/json"
    }

    override fun beforeBodyWrite(
            body: Any?,
            returnType: MethodParameter,
            selectedContentType: MediaType,
            selectedConverterType: Class<out HttpMessageConverter<*>>,
            request: ServerHttpRequest, response: ServerHttpResponse
    ): Any? {

        val result = mutableMapOf<String, Any?>(
                "status" to 1,
                "message" to localeService.getMessage("success")
        )

        if (isIterable(body)) {
            result["datas"] = body
        } else if (body is RestResponse) {
            result["status"] = body.status
            result["message"] = localeService.getMessage(body.message)

            if (isIterable(body.data)) {
                result["datas"] = body.data
            } else if (body.data != null) {
                result["data"] = body.data
            }

            if (body.meta != null) {
                result["meta"] = body.meta
            }
        } else {
            result["data"] = body
        }

        return result

    }
}