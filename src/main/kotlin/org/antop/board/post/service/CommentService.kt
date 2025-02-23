package org.antop.board.post.service

import kotlinx.datetime.LocalDateTime
import org.antop.board.common.Pagination
import org.antop.board.common.exceptions.CommentNotFoundException
import org.antop.board.common.exceptions.PostNotFoundException
import org.antop.board.common.extensions.now
import org.antop.board.post.dto.CommentView
import org.antop.board.post.mapper.toDto
import org.antop.board.post.model.Comment
import org.antop.board.post.model.Comments
import org.antop.board.post.model.Post
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService {
    @Transactional
    fun save(
        postId: Long,
        content: String,
    ): CommentView {
        val post = Post.findByIdAndUpdate(postId) { it.comments++ } ?: throw PostNotFoundException()
        val comment =
            Comment.new {
                this.post = post
                this.content = content
                created = LocalDateTime.now()
            }
        return comment.toDto()
    }

    @Transactional
    fun edit(
        commentId: Long,
        content: String,
    ): CommentView {
        val comment = Comment.findById(commentId) ?: throw CommentNotFoundException()
        comment.content = content
        return comment.toDto()
    }

    @Transactional
    fun remove(commentId: Long) {
        Comment.findById(commentId)?.let { comment ->
            comment.remove()
            Post.findByIdAndUpdate(comment.post.id.value) { it.comments-- }
        }
    }

    @Transactional(readOnly = true)
    fun getComments(
        postId: Long,
        before: Long = Long.MAX_VALUE,
    ): List<CommentView> =
        Comment
            .find { (Comments.post eq postId) and (Comments.removed eq false) and (Comments.id less before) }
            .orderBy(Comments.id to SortOrder.DESC)
            .limit(Pagination.DEFAULT_PAGE_SIZE)
            .map { it.toDto() }
}
