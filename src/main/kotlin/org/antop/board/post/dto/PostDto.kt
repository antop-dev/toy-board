package org.antop.board.post.dto

import kotlinx.datetime.LocalDateTime
import org.antop.board.file.dto.FileDto

data class PostDto(
    val id: Long,
    val subject: String,
    val content: String = "",
    val author: String,
    val changed: LocalDateTime,
    val tags: Set<String> = setOf(),
    val hits: Long,
    val files: List<FileDto> = emptyList(),
)
