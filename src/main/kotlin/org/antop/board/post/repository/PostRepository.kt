package org.antop.board.post.repository

import org.antop.board.common.exposed.contains
import org.antop.board.common.extensions.pretty
import org.antop.board.file.model.Files
import org.antop.board.member.model.Members
import org.antop.board.post.dto.PostQuery
import org.antop.board.post.dto.PostQueryColumn
import org.antop.board.post.model.Post
import org.antop.board.post.model.PostFiles
import org.antop.board.post.model.Posts
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.or
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class PostRepository {
    fun findPost(id: Long): Post? = Post.findById(id)

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
                Members.id.alias(PostQueryColumn.AUTHOR_ID),
                Members.nickname.alias(PostQueryColumn.AUTHOR_NICKNAME),
                Members.email.alias(PostQueryColumn.AUTHOR_EMAIL),
            ).where {
                createOp(keyword)
            }.orderBy(Posts.thread to SortOrder.DESC)
            .offset((page - 1) * pageSize)
            .limit(pageSize)
            .map { row ->
                val changed =
                    row.getOrNull(Posts.created.alias(PostQueryColumn.POST_MODIFIED))
                        ?: row[Posts.created.alias(PostQueryColumn.POST_CREATED)]
                PostQuery(
                    id = row[Posts.id.alias(PostQueryColumn.POST_ID)].value,
                    subject = row[Posts.subject.alias(PostQueryColumn.POST_SUBJECT)],
                    author =
                        PostQuery.Author(
                            id = row[Members.id.alias(PostQueryColumn.AUTHOR_ID)].value,
                            nickname = row[Members.nickname.alias(PostQueryColumn.AUTHOR_NICKNAME)],
                            email = row[Members.email.alias(PostQueryColumn.AUTHOR_EMAIL)],
                        ),
                    changed = changed.pretty(),
                    tags = row[Posts.tags.alias(PostQueryColumn.POST_TAGS)],
                    hits = row[Posts.hits.alias(PostQueryColumn.POST_HITS)],
                    depth = row[Posts.depth.alias(PostQueryColumn.POST_DEPTH)],
                    removed = row[Posts.removed.alias(PostQueryColumn.POST_REMOVED)],
                    comments = row[Posts.comments.alias(PostQueryColumn.POST_COMMENTS)],
                    likes = row[Posts.likes.alias(PostQueryColumn.POST_LIKES)],
                    dislikes = row[Posts.dislikes.alias(PostQueryColumn.POST_DISLIKES)],
                )
            }

    fun count(keyword: String?): Long =
        Posts
            .select(Posts.id)
            .where { createOp(keyword) }
            .count()

    private fun createOp(keyword: String?): Op<Boolean> =
        keyword?.let {
            Op.FALSE
                .or(Posts.subject contains it)
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
        } ?: Op.TRUE
}
