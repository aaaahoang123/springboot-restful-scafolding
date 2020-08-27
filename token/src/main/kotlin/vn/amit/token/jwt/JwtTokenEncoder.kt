package vn.amit.token.jwt

import io.jsonwebtoken.*
import vn.amit.token.TokenEncoder
import vn.amit.token.TokenSubject
import java.util.*
import javax.xml.bind.DatatypeConverter

class JwtTokenEncoder(
        secret: String,
        private val ttl: Long
) : TokenEncoder {
    private val apiKeySecretByte = DatatypeConverter.parseBase64Binary(secret)

    override fun encode(subject: TokenSubject): String {
        val now = Date()
        val expiryDate = Date(now.time + ttl)

        // Tạo chuỗi json web token từ id của user.
        return Jwts.builder()
                .setId(subject.getTokenIdentifier())
                .setSubject(subject.getTokenIdentifier())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .addClaims(subject.getCustomClaims())
                .signWith(SignatureAlgorithm.HS512, apiKeySecretByte)
                .compact()
    }

    @Throws(exceptionClasses = [MalformedJwtException::class, ExpiredJwtException::class, UnsupportedJwtException::class, IllegalArgumentException::class])
    override fun decode(token: String): String {
        return Jwts.parser()
                .setSigningKey(apiKeySecretByte)
                .parseClaimsJws(token)
                .body
                .subject
    }
}