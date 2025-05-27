package org.antop.board.common.spring

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import org.springframework.data.redis.serializer.RedisSerializer

class LocalDateTimeRedisSerializer : RedisSerializer<LocalDateTime> {
    override fun serialize(value: LocalDateTime?): ByteArray? = value?.format(LocalDateTime.Formats.ISO)?.toByteArray()

    override fun deserialize(bytes: ByteArray?): LocalDateTime? =
        bytes?.let {
            LocalDateTime.parse(String(it), LocalDateTime.Formats.ISO)
        }
}
