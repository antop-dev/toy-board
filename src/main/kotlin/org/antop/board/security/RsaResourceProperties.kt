package org.antop.board.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.io.Resource

@ConfigurationProperties(prefix = "app.rsa")
data class RsaResourceProperties(
    /** 개인키 파일 위치 */
    val privateKey: Resource,
    /** 공개키 파일 위치 */
    val publicKey: Resource,
)
