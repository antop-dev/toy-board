package org.antop.board.post.mapper

import org.antop.board.common.extensions.pretty
import org.antop.board.file.mapper.toDto
import org.antop.board.post.dto.PostDto
import org.antop.board.post.model.Post

fun Post.toDto(): PostDto {
    val changed = (modified ?: created)
    return PostDto(
        id = id.value,
        subject = subject,
        author = author,
        content = content,
        // Thymeleaf에서 kotlinx.date-time 타입을 지원하지 않아서 일단 java.time 으로 변경해서 줌
        changed = changed.pretty(),
        tags = tags,
        hits = hits,
        files = files.map { it.toDto() },
        depth = depth,
    )
}

/**
 * 목록 조회를 위해 내용과 첨부파일 목록은 제외하고 DTO 변환
 */
fun Post.toDtoForList(): PostDto {
    // 삭제된 게시글은 제목 노출 x
    val subject =
        when (removed) {
            true -> "삭제된 게시글"
            else -> subject
        }
    val changed = (modified ?: created)
    return PostDto(
        id = id.value,
        subject = subject,
        author = author,
        changed = changed.pretty(),
        tags = tags,
        hits = hits,
        depth = depth,
        removed = removed,
    )
}
