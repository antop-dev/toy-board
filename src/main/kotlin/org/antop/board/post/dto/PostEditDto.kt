package org.antop.board.post.dto

data class PostEditDto(
    val id: Long,
    val subject: String,
    val content: String,
    val author: String,
    val tags: Set<String>,
    val files: List<String> = emptyList(),
)
