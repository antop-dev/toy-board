package org.antop.board.member.mapper

import org.antop.board.member.dto.MemberDto
import org.antop.board.member.model.Member

fun Member.toDto(): MemberDto =
    MemberDto(
        id = this.id.value,
        email = this.email,
        password = this.password,
        nickname = this.nickname,
    )
