package org.antop.board.post.mapper

import org.antop.board.common.extensions.pretty
import org.antop.board.member.dto.MemberDto
import org.antop.board.post.dto.CommentDto
import org.antop.board.post.model.Comment

fun Comment.toDto(author: MemberDto): CommentDto {
    val changed = (modified ?: created)
    return CommentDto(
        id = id.value,
        content = content,
        author = author,
        changed = changed.pretty("yyyy.MM.dd HH:mm:ss"),
    )
}
