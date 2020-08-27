package vn.amit.security

interface SecurityProperties {
    fun getAuthenticatedResources(): List<String>
    fun getWebIgnoreResources(): List<String>
}