package org.antop.board.login

import org.antop.board.member.exception.MemberNotFoundException
import org.antop.board.member.service.MemberService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class LoginProcessingService(
    private val memberService: MemberService,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        try {
            val member = memberService.getMember(username)
            return UserPrincipal(
                id = member.id,
                email = member.email,
                _password = member.password,
                nickname = member.nickname,
            )
        } catch (e: MemberNotFoundException) {
            throw UsernameNotFoundException(username)
        }
    }
}
