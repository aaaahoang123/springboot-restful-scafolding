package vn.amit.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class RestSecurityEntryPoint : AuthenticationEntryPoint {
    @Throws(IOException::class, ServletException::class)
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, exception: AuthenticationException) {
        response.setHeader(HttpHeaders.CONTENT_TYPE, request.getHeader(HttpHeaders.ACCEPT) ?: "application/json")
        response.status = HttpStatus.UNAUTHORIZED.value()
        val mapper = ObjectMapper()
        response.writer.println(mapper.writeValueAsString(mapOf(
                "status" to 0,
                "message" to exception.message,
                "error" to exception.javaClass.name
        )))
    }
}