package org.antop.board.common.token

import java.time.Duration

interface TokenProvider {
    /**
     * 토큰 값을 생성한다.
     *
     * @param what 토큰을 생성할 값
     * @param expiration 토큰 만료시간
     */
    fun create(
        what: String,
        expiration: Duration = Duration.ZERO,
    ): String

    /**
     * 토큰을 파싱한다.
     */
    fun parse(token: String): String
}
