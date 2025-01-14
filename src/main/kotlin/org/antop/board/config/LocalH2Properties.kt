package org.antop.board.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * H2 로컬 서버 설정
 *
 * @property port 서버를 띄울 포트
 * @property database JDBC URL 중 데이터베이스 부분
 * @property init 스프링 구동 시 디비를 초기화 할지 여부
 */
@ConfigurationProperties(prefix = "local.server.h2")
data class LocalH2Properties(
    val port: Int,
    val database: String,
    val init: Boolean = false,
)
