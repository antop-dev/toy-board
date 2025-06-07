package org.antop.board.member.service

import extensions.now
import kotlinx.datetime.LocalDateTime
import org.antop.board.common.exceptions.EmailAlreadyExistsException
import org.antop.board.member.dto.MemberDto
import org.antop.board.member.exception.MemberNotFoundException
import org.antop.board.member.mapper.toDto
import org.antop.board.member.model.Member
import org.antop.board.member.repository.MemberRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Throws(MemberNotFoundException::class)
    @Transactional(readOnly = true)
    fun getMember(email: String): MemberDto {
        val member = memberRepository.findByEmail(email) ?: throw MemberNotFoundException(email)
        return member.toDto()
    }

    @Throws(MemberNotFoundException::class)
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
        avatar: String?,
    ): MemberDto {
        memberRepository.findByEmail(email)?.let {
            throw EmailAlreadyExistsException(email)
        }
        val member =
            Member.new {
                this.email = email
                this.password = passwordEncoder.encode(password)
                this.nickname = nickname
                this.avatar = avatar
                created = LocalDateTime.now()
            }
        return member.toDto()
    }

    @Transactional
    fun changePassword(
        email: String,
        password: String,
    ) {
        memberRepository.findByEmail(email)?.let { member ->
            member.password = passwordEncoder.encode(password)
        }
    }

    /**
     * 회원 정보를 수정합니다.
     *
     * @param email 회원의 이메일 주소
     * @param nickname 새로운 닉네임
     * @param avatar 새로운 아바타 URL (선택 사항)
     * @return 수정된 회원 정보 DTO
     */
    @Transactional
    fun modify(
        email: String,
        nickname: String,
        avatar: String?,
    ): MemberDto? {
        val member =
            memberRepository.findByEmail(email)?.let { member ->
                member.nickname = nickname
                member.avatar = avatar
                member.modified = LocalDateTime.now()
                member
            }
        return member?.toDto()
    }
}
