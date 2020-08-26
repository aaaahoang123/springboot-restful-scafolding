package vn.amit.common.cors

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CORSConfiguration : WebMvcConfigurer {
    @Value("\${vn.amit.cors.domains:http://localhost:4200}")
    private lateinit var domains: String

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedOrigins(*domains.split(",").toTypedArray())
                .allowedMethods("*")
    }
}