package org.antop.board.post.service

import kotlinx.datetime.LocalDateTime
import org.antop.board.common.Base62
import org.antop.board.common.Pagination
import org.antop.board.common.exposed.contains
import org.antop.board.common.exposed.jsonContains
import org.antop.board.common.extensions.now
import org.antop.board.common.extensions.toSizedIterable
import org.antop.board.file.model.File
import org.antop.board.file.model.Files
import org.antop.board.post.dto.PostDto
import org.antop.board.post.dto.PostEditDto
import org.antop.board.post.dto.PostSaveDto
import org.antop.board.post.mapper.toDto
import org.antop.board.post.mapper.toDtoForList
import org.antop.board.post.model.Post
import org.antop.board.post.model.PostFiles
import org.antop.board.post.model.Posts
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SortOrder
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
            .orderBy(Posts.id to SortOrder.DESC)
            .offset((page - 1) * pageSize)
            .limit(pageSize)
            .map { Post.wrapRow(it) }

    private fun joinClause() =
        Posts
            .leftJoin(PostFiles)
            .leftJoin(Files)

    private fun createOp(keyword: String?) =
        keyword?.let {
            Op.FALSE
                .or(Posts.subject contains it)
                .or(Posts.author contains it)
                .or(Posts.content contains it)
                .or(Posts.tags jsonContains it)
                .or(Files.name contains it)
        } ?: Op.TRUE

    /**
     * 게시물 한건 조회
     */
    fun get(id: Long): PostDto? = Post.findById(id)?.toDto()

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
                created = LocalDateTime.now()
                tags = saveDto.tags
                files = getFiles(saveDto.files)
            }
        return post.toDto()
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
            it.modified = LocalDateTime.now()
            it.tags = editDto.tags
            it.files = getFiles(editDto.files)
        }
    }

    /**
     * 게시물 삭제
     */
    @Transactional
    fun remove(id: Long) = Post.findById(id)?.delete()

    /**
     * 파일 아이디 목록으로 파일(엔티티) 목록 조회
     */
    private fun getFiles(fileIds: List<String>): SizedIterable<File> =
        fileIds
            .mapNotNull { fileId -> File.findById(Base62.decode(fileId)) }
            .toSizedIterable()
}
