package org.antop.admin.common.config

import org.antop.admin.common.security.LoggedUser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder
import java.util.Optional

@EnableJpaAuditing
@Configuration
class JpaConfig {
    /**
     * 현재 인증된 사용자의 식별자를 반환하는 AuditorAware를 제공.
     * 스프링 데이터 JPA의 엔티티 감사(Auditing) 기능에서 사용됨.
     *
     * @return 인증된 사용자의 ID를 Optional로 감싼 객체. 인증되지 않은 경우 0L 반환.
     */
    @Bean
    fun securityAuditorAware(): AuditorAware<Long> =
        AuditorAware<Long> {
            val principal = SecurityContextHolder.getContext().authentication?.principal
            val v =
                when (principal) {
                    is LoggedUser -> principal.id
                    else -> 0L
                }
            Optional.of(v)
        }
}
