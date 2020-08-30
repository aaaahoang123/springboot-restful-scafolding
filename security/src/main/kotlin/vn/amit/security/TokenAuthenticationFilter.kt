package vn.amit.security

import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.security.web.util.matcher.RequestMatcher
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.reflect.jvm.jvmName

class TokenAuthenticationFilter(
        requestMatcher: RequestMatcher,
        manager: AuthenticationManager,
        failureHandler: AuthenticationFailureHandler,
        private val userDetailService: TokenUserDetailService
): AbstractAuthenticationProcessingFilter(requestMatcher) {
    private val webDetailSource = WebAuthenticationDetailsSource()
    init {
        authenticationManager = manager
        setAuthenticationFailureHandler(failureHandler)
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        var token = request.getHeader(AUTHORIZATION)?.trim()
        if (token?.startsWith("Bearer ") != true) {
            throw BadCredentialsException("token_is_not_a_bearer_token")
        }
        token = token.replace("Bearer ", "")
        return try {
            val username = userDetailService.retrieveUsernameByToken(token)
            val user = userDetailService.loadUserByUsername(username)
            val authentication = UsernamePasswordAuthenticationToken(user, null, user.authorities)
            authentication.details = webDetailSource.buildDetails(request)
            authentication
        } catch (e: Exception) {
            logger.warn("Resolve the token: $token failed, reason: ${e.message}(${e::class.jvmName})")
            throw BadCredentialsException("wrong_authentication_info")
        }
    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        SecurityContextHolder.getContext().authentication = authResult
        chain.doFilter(request, response)
    }
}
