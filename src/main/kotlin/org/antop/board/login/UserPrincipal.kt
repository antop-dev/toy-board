package org.antop.board.login

import org.antop.board.member.dto.MemberDto
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal(
    private val _member: MemberDto,
) : UserDetails {
    companion object {
        private const val DEFAULT_ROLE_NAME = "ROLE_USER"
        val DEFAULT_ROLE = SimpleGrantedAuthority(DEFAULT_ROLE_NAME)
    }

    override fun getAuthorities() = mutableListOf(DEFAULT_ROLE)

    override fun getPassword() = _member.password

    override fun getUsername() = _member.email

    val member: MemberDto
        get() = _member.copy()
}
