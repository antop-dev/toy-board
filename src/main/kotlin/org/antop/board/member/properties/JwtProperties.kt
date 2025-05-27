package org.antop.board.member.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.jwt")
data class JwtProperties(
    /** 서명 비밀키 */
    val secretKey: String,
)
