package vn.amit.security

class SimpleSecurityProperties : SecurityProperties {
    override fun getAuthenticatedResources(): List<String> {
        return listOf("/api/**")
    }

    override fun getWebIgnoreResources(): List<String> {
        return listOf("/upload/**")
    }
}