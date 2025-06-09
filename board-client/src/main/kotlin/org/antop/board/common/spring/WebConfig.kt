package org.antop.board.common.spring

import extensions.InterceptorRegistry.plusAssign
import org.antop.board.common.valid.PasswordVerificationInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val passwordEncoder: PasswordEncoder,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry += PasswordVerificationInterceptor(passwordEncoder)
    }
}
