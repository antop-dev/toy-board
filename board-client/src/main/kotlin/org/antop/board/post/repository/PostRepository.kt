package org.antop.board.post.repository

import org.antop.board.common.exposed.contains
import org.antop.board.file.model.Files
import org.antop.board.member.model.Members
import org.antop.board.post.dto.AuthorQuery
import org.antop.board.post.dto.AuthorQueryColumn
import org.antop.board.post.dto.PostQuery
import org.antop.board.post.dto.PostQueryColumn
import org.antop.board.post.model.Post
import org.antop.board.post.model.PostFiles
import org.antop.board.post.model.Posts
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.or
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class PostRepository {
    /**
     * [id]로 게시글 한건 조회
     */
    fun findPost(id: Long): Post? = Post.findById(id)

    /**
     * 게시글 목록 조회
     */
    fun queryPosts(
        keyword: String?,
        page: Long,
        pageSize: Int,
    ): List<PostQuery> =
        Posts
            .join(
                Members,
                JoinType.INNER,
                onColumn = Posts.authorId,
                otherColumn = Members.id,
            ).select(
                Posts.id.alias(PostQueryColumn.POST_ID),
                Posts.subject.alias(PostQueryColumn.POST_SUBJECT),
                Posts.created.alias(PostQueryColumn.POST_CREATED),
                Posts.modified.alias(PostQueryColumn.POST_MODIFIED),
                Posts.hits.alias(PostQueryColumn.POST_HITS),
                Posts.tags.alias(PostQueryColumn.POST_TAGS),
                Posts.depth.alias(PostQueryColumn.POST_DEPTH),
                Posts.removed.alias(PostQueryColumn.POST_REMOVED),
                Posts.comments.alias(PostQueryColumn.POST_COMMENTS),
                Posts.likes.alias(PostQueryColumn.POST_LIKES),
                Posts.dislikes.alias(PostQueryColumn.POST_DISLIKES),
                Posts.secret.alias(PostQueryColumn.POST_SECRET),
                Members.id.alias(AuthorQueryColumn.AUTHOR_ID),
                Members.nickname.alias(AuthorQueryColumn.AUTHOR_NICKNAME),
                Members.email.alias(AuthorQueryColumn.AUTHOR_EMAIL),
            ).where {
                createOp(keyword)
            }.orderBy(Posts.thread to SortOrder.DESC)
            .offset((page - 1) * pageSize)
            .limit(pageSize)
            .map { row ->
                PostQuery(
                    id = row[Posts.id].value,
                    subject = row[Posts.subject],
                    author =
                        AuthorQuery(
                            id = row[Members.id].value,
                            nickname = row[Members.nickname],
                            email = row[Members.email],
                        ),
                    created = row[Posts.created],
                    modified = row[Posts.modified],
                    tags = row[Posts.tags],
                    hits = row[Posts.hits],
                    depth = row[Posts.depth],
                    removed = row[Posts.removed],
                    comments = row[Posts.comments],
                    likes = row[Posts.likes],
                    dislikes = row[Posts.dislikes],
                    secret = row[Posts.secret],
                )
            }

    /**
     * 게시글 카운트 조회
     */
    fun count(keyword: String?): Long =
        Posts
            .select(Posts.id)
            .where { createOp(keyword) }
            .count()

    /**
     * [keyword]로 조건문 만듦
     */
    private fun createOp(keyword: String?): Op<Boolean> =
        keyword?.let {
            Op.TRUE
                .and {
                    (Posts.subject contains it)
                        .or(Posts.content contains it)
                        .or(Posts.tags contains it)
                        .or(
                            exists(
                                PostFiles
                                    .innerJoin(Files)
                                    .select(PostFiles.post)
                                    .where { Files.name contains keyword },
                            ),
                        )
                }
        } ?: Op.TRUE
}
