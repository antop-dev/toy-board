package org.antop.board.post.dto

import org.antop.board.file.dto.FileDto
import java.time.LocalDateTime

data class PostDto(
    val id: Long,
    val subject: String,
    val content: String = "",
    val author: String,
    val changed: LocalDateTime,
    val changedPretty: String,
    val tags: Set<String> = setOf(),
    val hits: Long,
    val files: List<FileDto> = emptyList(),
)
