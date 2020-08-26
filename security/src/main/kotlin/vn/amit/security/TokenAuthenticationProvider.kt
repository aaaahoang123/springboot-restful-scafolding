package vn.amit.security

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class TokenAuthenticationProvider(
        @Qualifier(TOKEN_USER_DETAIL_SERVICE_BEAN_NAME) userDetailService: UserDetailsService
) : DaoAuthenticationProvider() {
    init {
        this.userDetailsService = userDetailService
    }

    override fun additionalAuthenticationChecks(userDetails: UserDetails, authentication: UsernamePasswordAuthenticationToken?) {
        //
    }
}
