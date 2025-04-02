package org.antop.board.member.service

import kotlinx.datetime.LocalDateTime
import org.antop.board.common.extensions.now
import org.antop.board.member.dto.MemberDto
import org.antop.board.member.exception.MemberNotFoundException
import org.antop.board.member.mapper.toDto
import org.antop.board.member.model.Member
import org.antop.board.member.repository.MemberRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Throws(UsernameNotFoundException::class)
    @Transactional(readOnly = true)
    fun getMember(email: String): MemberDto {
        val member = memberRepository.findByEmail(email) ?: throw MemberNotFoundException(email)
        return member.toDto()
    }

    @Throws(UsernameNotFoundException::class)
    @Transactional(readOnly = true)
    fun getMember(memberId: Long): MemberDto {
        val member = memberRepository.findById(memberId) ?: throw MemberNotFoundException("$memberId")
        return member.toDto()
    }

    @Transactional
    fun register(
        email: String,
        password: String,
        nickname: String,
    ): MemberDto {
        val entity =
            Member.new {
                this.email = email
                this.password = passwordEncoder.encode(password)
                this.nickname = nickname
                created = LocalDateTime.now()
            }
        return entity.toDto()
    }
}
