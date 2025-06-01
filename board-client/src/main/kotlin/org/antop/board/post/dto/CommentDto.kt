package org.antop.board.post.dto

import kotlinx.datetime.LocalDateTime
import org.antop.board.member.dto.MemberDto

data class CommentDto(
    val id: Long,
    val content: String,
    val author: MemberDto,
    val created: LocalDateTime,
    val modified: LocalDateTime?,
)
