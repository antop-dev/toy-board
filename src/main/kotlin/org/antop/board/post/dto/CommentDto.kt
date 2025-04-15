package org.antop.board.post.dto

import org.antop.board.member.dto.MemberDto

data class CommentDto(
    val id: Long,
    val content: String,
    val author: MemberDto,
    val changed: String,
)
