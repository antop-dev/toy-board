package org.antop.board.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * 레디스 로컬 서버 설정
 *
 * @property port 서버를 띄울 포트
 */
@ConfigurationProperties(prefix = "local.server.redis")
data class LocalRedisProperties(
    val port: Int = 46379,
)
