package org.antop.board.post.dto

import org.antop.board.file.dto.FileDto

data class PostDto(
    val id: Long,
    val subject: String,
    val content: String = "",
    val author: String,
    val changed: String,
    val tags: Set<String>? = null,
    val hits: Long,
    val files: List<FileDto> = emptyList(),
    val depth: Int = 0,
    val removed: Boolean = false,
    val comments: Int,
)
