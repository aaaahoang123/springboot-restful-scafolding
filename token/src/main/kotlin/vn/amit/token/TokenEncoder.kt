package vn.amit.token

interface TokenEncoder {
    /**
     * Will take the token subject and generate the token
     */
    fun encode(subject: TokenSubject): String

    /**
     * Take the token and return the token identifier
     */
    fun decode(token: String): String
}