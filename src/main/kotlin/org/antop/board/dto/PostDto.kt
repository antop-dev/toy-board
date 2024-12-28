package org.antop.board.dto

data class PostDto(
    val id: Long,
    val subject: String,
    val content: String,
    val author: String,
    val changeAt: String,
    val tags: Set<String> = setOf(),
)
