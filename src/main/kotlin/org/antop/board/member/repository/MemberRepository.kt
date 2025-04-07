package org.antop.board.member.repository

import org.antop.board.member.model.Member

interface MemberRepository {
    /**
     * [id]로 회원 조회
     */
    fun findById(id: Long): Member?

    /**
     * [email]로 회원 조회
     */
    fun findByEmail(email: String): Member?
}
