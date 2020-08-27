package vn.amit.common.utils

import java.security.SecureRandom
import java.util.*

fun randomStringBase64(): String {
    val secureRandom: SecureRandom = SecureRandom()
    val base64Encoder: Base64.Encoder = Base64.getUrlEncoder()
    val randomBytes = ByteArray(24)
    secureRandom.nextBytes(randomBytes)
    return base64Encoder.encodeToString(randomBytes)
}