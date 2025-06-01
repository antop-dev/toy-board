package org.antop.board.post.dto

import kotlinx.datetime.LocalDateTime
import org.antop.board.file.dto.FileDto

data class PostDto(
    val id: Long,
    val subject: String,
    val content: String,
    val authorId: Long,
    val created: LocalDateTime,
    val modified: LocalDateTime?,
    val tags: Set<String>? = null,
    val hits: Long = 0,
    val files: List<FileDto> = emptyList(),
    val depth: Int = 0,
    val removed: Boolean = false,
    val comments: Int = 0,
    val likes: Int = 0,
    val dislikes: Int = 0,
    val secret: Boolean = false,
    val parent: PostDto? = null,
)
