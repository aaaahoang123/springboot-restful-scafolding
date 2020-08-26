package vn.amit.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class SecurityDependenciesConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

//    @Bean
//    fun authenticationProvider(): AuthenticationProvider {
//        return TokenAuthenticationProvider()
//    }

//    @Bean
//    fun authenticationFailureHandler(): AuthenticationFailureHandler {
//        return SecurityFailureHandler()
//    }
}