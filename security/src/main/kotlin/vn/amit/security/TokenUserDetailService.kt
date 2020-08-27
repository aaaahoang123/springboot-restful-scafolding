package vn.amit.security

import org.springframework.security.core.userdetails.UserDetailsService

interface TokenUserDetailService : UserDetailsService {
    fun retrieveUsernameByToken(token: String): String
}