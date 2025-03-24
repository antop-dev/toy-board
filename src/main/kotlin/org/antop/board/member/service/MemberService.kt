package org.antop.board.member.service

import jakarta.validation.ValidationException
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

    @Transactional
    fun register(
        email: String,
        password1: String,
        password2: String,
        nickname: String,
    ): MemberDto {
        if (password1 != password2) {
            throw ValidationException("비밀번호가 일치하지 않습니다.")
        }
        val entity =
            Member.new {
                this.email = email
                this.password = passwordEncoder.encode(password1)
                this.nickname = nickname
                created = LocalDateTime.now()
            }
        return entity.toDto()
    }
}
