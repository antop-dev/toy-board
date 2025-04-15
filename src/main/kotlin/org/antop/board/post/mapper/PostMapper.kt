package org.antop.board.post.mapper

import org.antop.board.common.extensions.pretty
import org.antop.board.file.mapper.toDto
import org.antop.board.post.dto.PostDto
import org.antop.board.post.model.Post

fun Post.toDto(parent: PostDto? = null): PostDto {
    val changed = (modified ?: created)
    return PostDto(
        id = id.value,
        subject = subject,
        authorId = authorId,
        content = content,
        changed = changed.pretty(),
        tags = tags,
        hits = hits,
        files = files.map { it.toDto() },
        depth = depth,
        comments = comments,
        likes = likes,
        dislikes = dislikes,
        secret = secret,
        parent = parent,
    )
}
