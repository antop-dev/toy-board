package org.antop.board.post.service

/**
 * 게시글 저장/수정/답글 요청 DTO
 */
data class PostSaveServiceRequest(
    val subject: String,
    val content: String,
    val authorId: Long,
    val tags: Set<String>?,
    val files: List<String>?,
)
