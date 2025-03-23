package org.antop.board.member.repository

import org.antop.board.member.model.Member
import org.antop.board.member.model.Members
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class MemberRepository {
    @Transactional(readOnly = true)
    fun findByEmail(email: String) =
        Member
            .find { Members.email eq email }
            .firstOrNull()
}
