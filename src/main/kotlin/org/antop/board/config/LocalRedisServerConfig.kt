package org.antop.board.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import redis.embedded.RedisServer

@Profile("local")
@Configuration
@ConditionalOnClass(RedisAutoConfiguration::class)
@AutoConfigureBefore(RedisAutoConfiguration::class)
class LocalRedisServerConfig(
    private val localRedisProperties: LocalRedisProperties,
) {
    private val log = KotlinLogging.logger { }

    @Bean(initMethod = "start", destroyMethod = "stop")
    fun redis(): RedisServer =
        RedisServer(localRedisProperties.port).apply {
            log.info { "Redis local server = ${this.ports()}" }
        }
}
