package vn.amit.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableWebSecurity
@EnableTransactionManagement(proxyTargetClass=true)
@EnableGlobalMethodSecurity(jsr250Enabled = true, securedEnabled = true)
class RestSecurityConfig @Autowired constructor(
    private val entryPoint: RestSecurityEntryPoint,
    private val accessDeniedHandler: CustomAccessDeniedHandler,
    private val failureHandler: SecurityFailureHandler,
    private val securityProperties: SecurityProperties
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(entryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .authorizeRequests()
                .antMatchers(*securityProperties.getAuthenticatedResources().toTypedArray())
                .authenticated()
                .and()
                .cors()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable()

        securityProperties.getAuthenticatedResources()
                .forEach {
                    http.addFilterBefore(
                            TokenAuthenticationFilter(AntPathRequestMatcher(it), authenticationManager(), failureHandler),
                            AnonymousAuthenticationFilter::class.java
                    )
                }

//        return filter
//        http
//            .authenticationProvider(provider)
//            .addFilterBefore(
//                TokenAuthenticationFilter(AntPathRequestMatcher("/**"), authenticationManager(), authService),
//                AnonymousAuthenticationFilter::class.java
//            )

//            .addFilterBefore(
//                TokenAuthenticationFilter(AntPathRequestMatcher("$API_PREFIX/admin/**"), authenticationManager(), authService),
//                AnonymousAuthenticationFilter::class.java
//            )
    }

    override fun configure(web: WebSecurity) {
        web.ignoring()
                .antMatchers(*securityProperties.getWebIgnoreResources().toTypedArray())
    }
}
