package vn.amit.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import vn.amit.common.language.LocaleService
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SecurityFailureHandler @Autowired constructor(
        private val localeService: LocaleService
) : AuthenticationFailureHandler {
    private val mapper = ObjectMapper()

    @Throws(exceptionClasses = [IOException::class, ServletException::class])
    override fun onAuthenticationFailure(request: HttpServletRequest, response: HttpServletResponse, exception: AuthenticationException) {
        response.setHeader(HttpHeaders.CONTENT_TYPE, request.getHeader(HttpHeaders.ACCEPT) ?: "application/json")
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.writer.println(mapper.writeValueAsString(mapOf(
                "status" to 0,
                "message" to if (exception.message != null) localeService.getMessage(exception.message!!) else "Failed",
                "error" to exception.javaClass.name
        )))
    }
}