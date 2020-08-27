package vn.amit.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

abstract class TokenUserDetailService : UserDetailsService {
    protected val logger: Logger = LoggerFactory.getLogger(this::class.java)

    protected abstract fun loadByToken(token: String): UserDetails

    override fun loadUserByUsername(token: String): UserDetails {
//        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request

        return try {
            loadByToken(token)
        } catch (e: Exception) {
            if (logger.isDebugEnabled) {
                logger.debug("Decode the token: $token failed, reason: " + e.message)
            }
            throw UsernameNotFoundException("wrong_authentication_info")
        }
    }
}