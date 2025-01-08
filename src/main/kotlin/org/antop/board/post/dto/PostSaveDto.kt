package org.antop.board.post.dto

data class PostSaveDto(
    val subject: String,
    val content: String,
    val author: String,
    val tags: Set<String> = emptySet(),
    val files: List<String> = emptyList(),
)
