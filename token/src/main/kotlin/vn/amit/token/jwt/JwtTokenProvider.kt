package vn.amit.token.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import vn.amit.token.TokenEncoder

@Configuration
@ConfigurationProperties(prefix = "vn.amit.token.jwt")
class JwtTokenProvider {
//    @Value("\${vn.amit.token.jwt.secret:VBBghy7q2qssSVg9rIL9l1T0cPkfaDkqeLNr2AwPJxWrd7rosUWuFFPMCat1CpKY}")
    lateinit var secret: String
//    @Value("\${vn.amit.token.jwt.ttl:604800000}")
    var ttl: Long = 0

    @Bean
    fun tokenEncoder(): TokenEncoder {
        return JwtTokenEncoder(secret, ttl)
    }
}