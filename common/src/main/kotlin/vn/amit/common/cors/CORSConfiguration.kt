package vn.amit.common.cors

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ConfigurationProperties(prefix = "vn.amit.cors")
class CORSConfiguration : WebMvcConfigurer {
    var domains: String? = null

    override fun addCorsMappings(registry: CorsRegistry) {
        domains?.let {
            registry.addMapping("/**")
                    .allowedOrigins(*it.split(",").toTypedArray())
                    .allowedMethods("*")
        }
    }
}