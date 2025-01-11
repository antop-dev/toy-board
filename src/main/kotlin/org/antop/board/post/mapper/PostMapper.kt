package org.antop.board.post.mapper

import org.antop.board.file.mapper.toDto
import org.antop.board.post.dto.PostDto
import org.antop.board.post.model.Post

fun Post.toDto(): PostDto =
    PostDto(
        id = id.value,
        subject = subject,
        author = author,
        content = content,
        changed = modified ?: created,
        tags = tags,
        hits = hits,
        files = files.map { it.toDto() },
    )

/**
 * 목록 조회를 위해 내용과 첨부파일 목록은 제외하고 DTO 변환
 */
fun Post.toDtoForList(): PostDto =
    PostDto(
        id = id.value,
        subject = subject,
        author = author,
        changed = modified ?: created,
        tags = tags,
        hits = hits,
    )
