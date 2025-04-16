package org.antop.board.post.service

import kotlinx.datetime.LocalDateTime
import org.antop.board.common.Base62
import org.antop.board.common.Pagination
import org.antop.board.common.exceptions.PostNotFoundException
import org.antop.board.common.extensions.now
import org.antop.board.common.extensions.toSizedIterable
import org.antop.board.file.model.File
import org.antop.board.post.dto.PostDto
import org.antop.board.post.dto.PostQuery
import org.antop.board.post.mapper.toDto
import org.antop.board.post.model.Post
import org.antop.board.post.model.Posts
import org.antop.board.post.repository.PostRepository
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.max
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository,
) {
    /**
     * 게시물 조회
     */
    fun getPosts(
        keyword: String? = null,
        page: Long = 1,
        pageSize: Int = 10,
    ): Pagination.Response<PostQuery> {
        val total = postRepository.count(keyword)
        val posts =
            when {
                total > 0 -> postRepository.queryPosts(keyword, page, pageSize)
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
    fun getPost(id: Long): PostDto {
        val post = postRepository.findPost(id) ?: throw PostNotFoundException()
        if (post.removed) {
            throw PostNotFoundException()
        }
        return post.toDto()
    }

    /**
     * 게시물 저장
     */
    @Transactional
    fun save(saveDto: PostSaveServiceRequest): PostDto {
        val post =
            Post.new {
                subject = saveDto.subject
                content = saveDto.content
                authorId = saveDto.authorId
                created = LocalDateTime.now()
                tags = saveDto.tags
                files = getFiles(saveDto.files)
                thread = nextThread()
            }
        return post.toDto()
    }

    private fun nextThread(): Long {
        val alias = Posts.thread.max().alias("max_thread")
        val maxThread = Posts.select(alias).singleOrNull()?.get(alias) ?: 0
        return maxThread + 1000
    }

    /**
     * 게시물 저장
     */
    @Transactional
    fun reply(
        parentPostId: Long,
        request: PostSaveServiceRequest,
    ): PostDto {
        val parentPost = Post.findById(parentPostId) ?: throw PostNotFoundException()
        // parentPost 의 thread 보다 낮았던 게시글들의 depth를 다 -1씩 낮춘다.
        Post
            .find(Posts.thread.between(parentPost.minThread(), parentPost.thread - 1))
            .forEach { it.thread -= 1 }

        val replyPost =
            Post.new {
                subject = request.subject
                content = request.content
                authorId = request.authorId
                created = LocalDateTime.now()
                tags = request.tags
                files = getFiles(request.files)
                thread = parentPost.thread - 1
                depth = parentPost.depth + 1
            }
        return replyPost.toDto()
    }

    /**
     * 게시물 수정
     */
    @Transactional
    fun edit(
        postId: Long,
        editDto: PostSaveServiceRequest,
    ) = findForUpdate(postId) {
        it.subject = editDto.subject
        it.content = editDto.content
        it.authorId = editDto.authorId
        it.modified = LocalDateTime.now()
        it.tags = editDto.tags
        it.files = getFiles(editDto.files)
    }

    /**
     * 게시물 삭제
     */
    @Transactional
    fun remove(id: Long) {
        Post.findById(id)?.remove()
    }

    /**
     * 파일 아이디 목록으로 파일(엔티티) 목록 조회
     */
    private fun getFiles(fileIds: List<String>?): SizedIterable<File> =
        fileIds
            ?.mapNotNull { fileId -> File.findById(Base62.decode(fileId)) }
            ?.toSizedIterable()
            ?: SizedCollection()

    /**
     * 게시글 좋아요 카운트++
     */
    @Transactional
    fun like(postId: Long) = findForUpdate(postId) { it.likes++ }

    /**
     * 게시글 싫어요 카운트++
     */
    @Transactional
    fun dislike(postId: Long) = findForUpdate(postId) { it.dislikes++ }

    /**
     * 수정하기 위한 조회. 내부적으로 해당 데이터에 락이 걸린다.
     */
    @Transactional
    fun findForUpdate(
        postId: Long,
        action: (Post) -> Unit = {
        },
    ) = Post.findByIdAndUpdate(postId) { action(it) }?.toDto() ?: throw PostNotFoundException()
}
