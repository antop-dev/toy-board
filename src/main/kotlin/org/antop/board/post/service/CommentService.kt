package org.antop.board.post.service

import kotlinx.datetime.LocalDateTime
import org.antop.board.common.exceptions.CommentNotFoundException
import org.antop.board.common.exceptions.PostNotFoundException
import org.antop.board.common.extensions.now
import org.antop.board.member.service.MemberService
import org.antop.board.post.dto.CommentDto
import org.antop.board.post.dto.CommentQuery
import org.antop.board.post.mapper.toDto
import org.antop.board.post.model.Comment
import org.antop.board.post.model.Post
import org.antop.board.post.repository.CommentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val memberService: MemberService,
    private val commentRepository: CommentRepository,
) {
    @Transactional
    fun save(
        postId: Long,
        content: String,
        authorId: Long,
    ): CommentDto {
        val post = Post.findByIdAndUpdate(postId) { it.comments++ } ?: throw PostNotFoundException()
        val comment =
            Comment.new {
                this.post = post
                this.content = content
                this.authorId = authorId
                created = LocalDateTime.now()
            }
        val author = memberService.getMember(comment.authorId)
        return comment.toDto(author)
    }

    @Transactional
    fun edit(
        commentId: Long,
        content: String,
    ): CommentDto {
        val comment = Comment.findById(commentId) ?: throw CommentNotFoundException()
        comment.content = content

        val author = memberService.getMember(comment.authorId)
        return comment.toDto(author)
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
    ): List<CommentQuery> = commentRepository.queryCommentsByPost(postId, before)
}
