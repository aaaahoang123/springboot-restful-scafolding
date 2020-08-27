package vn.amit.token.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import vn.amit.token.TokenEncoder

@Configuration
@ConfigurationProperties(prefix = "vn.amit.token.jwt")
class JwtTokenProvider {
    var secret: String = "VBBghy7q2qssSVg9rIL9l1T0cPkfaDkqeLNr2AwPJxWrd7rosUWuFFPMCat1CpKY"
    var ttl: Long = 604800000

    @Bean
    fun tokenEncoder(): TokenEncoder {
        return JwtTokenEncoder(secret, ttl)
    }
}