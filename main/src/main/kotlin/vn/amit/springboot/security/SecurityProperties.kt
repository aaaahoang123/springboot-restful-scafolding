package vn.amit.springboot.security

import org.springframework.stereotype.Component
import vn.amit.security.SecurityProperties as BaseSecurityProperties

@Component
class SecurityProperties : BaseSecurityProperties {
    override fun getAuthenticatedResources(): List<String> {
        return listOf("/api/v1/**")
    }

    override fun getWebIgnoreResources(): List<String> {
        return listOf("/upload/**", "/upload")
    }
}