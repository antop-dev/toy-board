package org.antop.board.post.dto

import kotlinx.datetime.LocalDateTime

data class CommentQuery(
    val id: Long,
    val content: String,
    val author: AuthorQuery,
    val created: LocalDateTime,
    val modified: LocalDateTime?,
)
