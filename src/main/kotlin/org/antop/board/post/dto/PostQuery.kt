package org.antop.board.post.dto

import kotlinx.datetime.LocalDateTime

data class PostQuery(
    val id: Long,
    val subject: String,
    val author: AuthorQuery,
    val created: LocalDateTime,
    val changed: String,
    val tags: Set<String>? = null,
    val hits: Long = 0,
    val depth: Int = 0,
    val removed: Boolean = false,
    val comments: Int,
    val likes: Int = 0,
    val dislikes: Int = 0,
)
