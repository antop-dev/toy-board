package org.antop.board.dto

data class PostSaveDto(
    val subject: String,
    val content: String,
    val author: String,
    val tags: Set<String>,
)
