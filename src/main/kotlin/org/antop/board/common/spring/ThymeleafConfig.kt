package org.antop.board.common.spring

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect
import org.antop.board.common.thymeleaf.KotlinxDateTimeDialect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ThymeleafConfig {
    @Bean
    fun layoutDialect(): LayoutDialect = LayoutDialect()

    @Bean
    fun kotlinxDateTimeDialect() = KotlinxDateTimeDialect()
}
