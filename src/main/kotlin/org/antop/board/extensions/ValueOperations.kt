package org.antop.board.extensions

import org.springframework.data.redis.core.ValueOperations
import java.util.concurrent.TimeUnit

/**
 * 코틀린 문법으로 `[key, timeout, unit] = value`를 사용할 수 있도록 해주는 확장 함수
 */
operator fun <K : Any, V : Any> ValueOperations<K, V>.set(
    key: K,
    timeout: Long,
    unit: TimeUnit,
    value: V,
) {
    this[key, value, timeout] = unit
}
