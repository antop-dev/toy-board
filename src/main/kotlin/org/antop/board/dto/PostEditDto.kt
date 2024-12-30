package org.antop.board.dto

data class PostEditDto(
    val id: Long,
    val subject: String,
    val content: String,
    val author: String,
    val tags: Set<String>,
)
