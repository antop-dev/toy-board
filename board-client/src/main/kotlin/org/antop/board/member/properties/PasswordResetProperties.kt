package org.antop.board.member.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "app.password-reset")
data class PasswordResetProperties(
    /** 비밀번호 리셋 토클 유효시간 */
    val tokenTimeout: Duration,
)
