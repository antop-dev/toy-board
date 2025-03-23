package org.antop.board.login

import org.antop.board.member.exception.MemberNotFoundException
import org.antop.board.member.service.MemberService
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class LoginProcessingService(
    private val memberService: MemberService,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        try {
            val member = memberService.getMember(username)
            return object : UserDetails {
                override fun getAuthorities() = mutableListOf(SimpleGrantedAuthority("ROLE_USER"))

                override fun getPassword() = member.password

                override fun getUsername() = member.email
            }
        } catch (e: MemberNotFoundException) {
            throw UsernameNotFoundException(username)
        }
    }
}
