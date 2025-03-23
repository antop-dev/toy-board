package org.antop.board.member.service

import org.antop.board.member.dto.MemberDto
import org.antop.board.member.exception.MemberNotFoundException
import org.antop.board.member.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
) {
    fun getMember(email: String): MemberDto {
        val member = memberRepository.findByEmail(email) ?: throw MemberNotFoundException(email)
        return MemberDto(
            id = member.id.value,
            email = member.email,
            password = member.password,
            nickname = member.nickname,
        )
    }
}
