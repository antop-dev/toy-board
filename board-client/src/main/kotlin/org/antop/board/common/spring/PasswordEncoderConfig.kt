package org.antop.board.common.spring

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class PasswordEncoderConfig {
    // MemberService 빈과 SecurityConfig.passwordEncoder 빈이 순환 참조가 생긴다.
    // passwordEncoder 빈 설정을 별도로 뺀다.
    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
}
