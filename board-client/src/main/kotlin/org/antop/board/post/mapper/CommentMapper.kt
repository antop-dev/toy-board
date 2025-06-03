package org.antop.board.post.mapper

import org.antop.board.member.dto.MemberDto
import org.antop.board.post.dto.CommentDto
import org.antop.board.post.model.Comment

fun Comment.toDto(author: MemberDto): CommentDto =
    CommentDto(
        id = id.value,
        content = content,
        author = author,
        created = created,
        modified = modified,
    )
