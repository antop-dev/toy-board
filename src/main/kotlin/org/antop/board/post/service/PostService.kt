package org.antop.board.post.service

import kotlinx.datetime.LocalDateTime
import org.antop.board.common.Base62
import org.antop.board.common.Pagination
import org.antop.board.common.exceptions.PostNotFoundException
import org.antop.board.common.exposed.contains
import org.antop.board.common.extensions.now
import org.antop.board.common.extensions.toSizedIterable
import org.antop.board.file.model.File
import org.antop.board.file.model.Files
import org.antop.board.post.dto.PostDto
import org.antop.board.post.mapper.toDto
import org.antop.board.post.mapper.toDtoForList
import org.antop.board.post.model.Post
import org.antop.board.post.model.PostFiles
import org.antop.board.post.model.Posts
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.or
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService {
    /**
     * 게시물 조회
     */
    fun getPosts(
        keyword: String?,
        page: Long,
        pageSize: Int,
    ): Pagination.Response<PostDto> {
        val total = countQuery(keyword)
        val posts = if (total > 0) selectQuery(keyword, page, pageSize) else listOf()
        return Pagination.Response(
            items = posts.map { it.toDtoForList() },
            total = total,
        )
    }

    private fun countQuery(keyword: String?): Long =
        joinClause()
            .select(Posts.id)
            .where { createOp(keyword) }
            .withDistinct()
            .count()

    private fun selectQuery(
        keyword: String?,
        page: Long,
        pageSize: Int,
    ): List<Post> =
        joinClause()
            .select(Posts.fields)
            .where { createOp(keyword) }
            .withDistinct()
            .orderBy(Posts.thread to SortOrder.DESC)
            .offset((page - 1) * pageSize)
            .limit(pageSize)
            .map { Post.wrapRow(it) }

    private fun joinClause() =
        Posts
            .leftJoin(PostFiles)
            .leftJoin(Files)

    private fun createOp(keyword: String?): Op<Boolean> =
        keyword?.let {
            Op.FALSE
                .or(Posts.subject contains it)
                .or(Posts.author contains it)
                .or(Posts.content contains it)
                .or(Posts.tags contains it)
                .or(Files.name contains it)
        } ?: Op.TRUE

    /**
     * 게시물 한건 조회
     */
    fun getPost(id: Long): PostDto {
        val post = Post.findById(id) ?: throw PostNotFoundException()
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
                author = saveDto.author
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
                author = request.author
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
    ): PostDto {
        val editedPost =
            Post.findByIdAndUpdate(postId) {
                it.subject = editDto.subject
                it.content = editDto.content
                it.author = editDto.author
                it.modified = LocalDateTime.now()
                it.tags = editDto.tags
                it.files = getFiles(editDto.files)
            } ?: throw PostNotFoundException()
        return editedPost.toDto()
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
}
