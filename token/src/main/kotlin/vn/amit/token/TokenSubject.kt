package vn.amit.token

interface TokenSubject {
    /**
     * Get the identifier that will be stored in the subject claim of the JWT.
     *
     * @return String
     */
    fun getTokenIdentifier(): String

    /**
     * Return a key value array, containing any custom claims to be added to the JWT.
     *
     * @return Map<String, Any>
     */
    fun getCustomClaims(): Map<String, Any> {
        return mapOf()
    }
}