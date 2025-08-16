package org.antop.admin.common.security

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * Spring Security의 `UserDetails`를 구현한 클래스입니다.
 *
 * 이 클래스는 `AdminUserDetailsService`를 통해 어드민 계정을 기반으로 생성되며,
 * Spring Security 인증 절차에서 사용됩니다. 주로 사용자 권한 검사, 계정 만료 여부,
 * 활성 상태 등을 확인하는 데 사용됩니다.
 *
 * @property id 어드민의 고유 식별자
 * @property name 어드민의 사용자명
 * @constructor 어드민 계정 정보를 바탕으로 `LoggedUser` 객체를 생성합니다.
 * @param _username 어드민의 로그인 ID
 * @param _password 어드민의 비밀번호
 *
 * 구현된 메서드:
 * - `getAuthorities`: 어드민의 기본 권한을 반환합니다. (기본적으로 ROLE_ADMIN)
 * - `getPassword`: 사용자의 비밀번호를 반환합니다.
 * - `getUsername`: 사용자의 로그인 ID를 반환합니다.
 * - `isAccountNonExpired`: 계정 만료 여부를 반환합니다. (`true`로 설정됨)
 * - `isAccountNonLocked`: 계정 잠금 여부를 반환합니다. (`true`로 설정됨)
 * - `isCredentialsNonExpired`: 자격 증명 만료 여부를 반환합니다. (`true`로 설정됨)
 * - `isEnabled`: 계정 활성 여부를 반환합니다. (`true`로 설정됨)
 */
class LoggedUser(
    /** 어드민ID */
    val id: Long,
    /** 어드민 사용자명 */
    val name: String,
    /** 로그인 아이디 */
    private val _username: String,
    /** 비밀번호 */
    private val _password: String,
) : UserDetails {
    override fun getAuthorities() = listOf(SimpleGrantedAuthority("ROLE_ADMIN"))

    override fun getPassword() = _password

    override fun getUsername() = _username

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}
