package org.antop.board.post.dto

data class CommentQuery(
    val id: Long,
    val content: String,
    val author: AuthorQuery,
    val changed: String,
)
