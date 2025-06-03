package org.antop.board.common.token

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.antop.board.member.properties.JwtProperties
import org.apache.commons.codec.binary.Hex
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

@Component
class JwtTokenProvider(
    private val jwtProperties: JwtProperties,
) : TokenProvider {
    override fun create(
        what: String,
        expiration: Duration,
    ): String {
        val key = Keys.hmacShaKeyFor(jwtProperties.secretKey.toByteArray())
        val token =
            Jwts
                .builder()
                .apply {
                    subject(what)
                    if (expiration != Duration.ZERO) {
                        val exp = LocalDateTime.now().plus(expiration)
                        val date = Date.from(exp.atZone(ZoneId.systemDefault()).toInstant())
                        expiration(date)
                    }
                    signWith(key, Jwts.SIG.HS256)
                }.compact()

        return Hex.encodeHexString(token.toByteArray())
    }

    override fun parse(token: String): String {
        val decoded = String(Hex.decodeHex(token))
        val key = Keys.hmacShaKeyFor(jwtProperties.secretKey.toByteArray())

        return try {
            Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(decoded) // 만료되면 ExpiredJwtException 예외 발생한다.
                .payload
                .subject
        } catch (e: ExpiredJwtException) {
            throw e
        }
    }
}
