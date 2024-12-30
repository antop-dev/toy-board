package org.antop.board.service

import kotlinx.datetime.LocalDateTime
import org.antop.board.common.Pagination
import org.antop.board.dto.PostDto
import org.antop.board.dto.PostEditDto
import org.antop.board.dto.PostSaveDto
import org.antop.board.extensions.now
import org.antop.board.extensions.pretty
import org.antop.board.model.Post
import org.antop.board.model.Posts
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.or
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService {
    /**
     * 게시물 조회
     */
    fun list(
        keyword: String?,
        page: Long,
        pageSize: Int,
    ): Pagination.Response<PostDto> {
        // 검색 조건
        val op =
            keyword?.let {
                Posts.subject
                    .like("%$keyword%")
                    .or(Posts.author.like("%$keyword%"))
            } ?: Op.TRUE

        val total = Post.count(op)
        val posts =
            when {
                (total > 0) ->
                    Post
                        .find(op)
                        .orderBy(Posts.id to SortOrder.DESC)
                        .offset((page - 1) * pageSize)
                        .limit(pageSize)
                        .map { toDto(it) }

                else -> listOf()
            }

        return Pagination.Response(
            items = posts,
            total = total,
        )
    }

    /**
     * 게시물 한건 조회
     */
    fun get(id: Long): PostDto? = Post.findById(id)?.let { toDto(it) }

    /**
     * 게시물 저장
     */
    @Transactional
    fun save(saveDto: PostSaveDto): PostDto {
        val post =
            Post.new {
                subject = saveDto.subject
                content = saveDto.content
                author = saveDto.author
                createdAt = LocalDateTime.now()
            }
        return toDto(post)
    }

    /**
     * 게시물 수정
     */
    @Transactional
    fun edit(editDto: PostEditDto) {
        Post.findByIdAndUpdate(editDto.id) {
            it.subject = editDto.subject
            it.content = editDto.content
            it.author = editDto.author
            it.modifiedAt = LocalDateTime.now()
        }
    }

    /**
     * 게시물 삭제
     */
    @Transactional
    fun remove(id: Long) {
        Post.findById(id)?.delete()
    }

    /**
     * Entity → DTO 변환
     */
    private fun toDto(post: Post): PostDto =
        PostDto(
            id = post.id.value,
            subject = post.subject,
            content = post.content,
            author = post.author,
            changeAt = post.changeAt.pretty(),
            hits = post.hits,
        )
}
