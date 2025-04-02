package org.antop.board.login

import org.antop.board.member.dto.MemberDto
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal(
    private val _member: MemberDto,
) : UserDetails {
    override fun getAuthorities() = mutableListOf(SimpleGrantedAuthority("ROLE_USER"))

    override fun getPassword() = _member.password

    override fun getUsername() = _member.email

    val member: MemberDto
        get() = _member.copy()
}
