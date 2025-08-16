package org.antop.admin.common.jooq

import org.antop.admin.common.security.LoggedUser
import org.springframework.security.core.context.SecurityContextHolder

/**
 * Spring Security의 컨텍스트에서 현재 인증된 사용자의 정보를 기반으로 감사(audit) 데이터를 제공하는 AuditorAware 구현체입니다.
 * 이 클래스는 SecurityContextHolder를 통해 인증된 사용자의 정보를 가져오며,
 * 현재 사용자의 ID를 반환하여 'created_by' 또는 'modified_by'와 같은 필드에 활용될 수 있습니다.
 *
 * Spring Data JPA 또는 JOOQ의 감사 기능을 활성화할 때 사용되며,
 * 사용자 활동 기록을 정확히 추적하는 데 도움을 줍니다.
 *
 * `SecurityContextHolder`에서 인증 정보를 추출하여 AuditorAware 인터페이스의 getCurrentAuditor 메서드를 구현합니다.
 */
class SpringSecurityAuditorAware : AuditorAware<Long> {
    override fun getCurrentAuditor(): Long {
        val loggedUser = SecurityContextHolder.getContext().authentication.principal as LoggedUser
        return loggedUser.id
    }
}
