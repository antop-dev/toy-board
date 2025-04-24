package org.antop.board.config

import org.antop.board.common.thymeleaf.KotlinxDateTimeDialect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ThymeleafConfig {
    @Bean
    fun kotlinxDateTimeDialect() = KotlinxDateTimeDialect()
}
