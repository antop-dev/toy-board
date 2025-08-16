package org.antop.admin.common.config

import org.antop.admin.common.jooq.AuditRecordListener
import org.antop.admin.common.jooq.AuditorAware
import org.antop.admin.common.jooq.SpringSecurityAuditorAware
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * JOOQ 구성 설정을 담당하는 클래스입니다. Spring 컨테이너에 필요한 Bean을 등록하며,
 * 주로 JOOQ DSLContext와 감사 기능을 위한 AuditorAware 구현체를 제공합니다.
 *
 * dslContext 메서드는 JOOQ의 DSLContext를 생성하여 반환하며, AuditRecordListener를 설정하여
 * 레코드의 생성 및 수정 작업에서 감사 정보를 자동으로 기록할 수 있도록 합니다.
 *
 * auditorAware 메서드는 현재 인증된 사용자의 정보를 제공하는 AuditorAware<Long>을 반환합니다.
 * Spring Security를 기반으로 사용자 ID를 제공하며, 감사 필드(created_by, modified_by 등)에 사용됩니다.
 */
@Configuration
class JooqConfig {
    @Bean
    fun dslContext(configuration: org.jooq.Configuration): DSLContext {
        configuration.set(AuditRecordListener(auditorAware()))
        return DSL.using(configuration)
    }

    @Bean
    fun auditorAware(): AuditorAware<Long> = SpringSecurityAuditorAware()
}
