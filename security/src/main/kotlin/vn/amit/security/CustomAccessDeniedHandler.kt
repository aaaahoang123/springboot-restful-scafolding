package vn.amit.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CustomAccessDeniedHandler: AccessDeniedHandler {
    override fun handle(request: HttpServletRequest, response: HttpServletResponse, accessDeniedException: AccessDeniedException) {
        response.setHeader(HttpHeaders.CONTENT_TYPE, request.getHeader(HttpHeaders.ACCEPT) ?: "application/json")
        response.status = HttpStatus.FORBIDDEN.value()
        val mapper = ObjectMapper()
        response.writer.println(mapper.writeValueAsString(mapOf(
            "status" to 0,
            "message" to accessDeniedException.message,
            "error" to accessDeniedException.javaClass.name
        )))
    }
}