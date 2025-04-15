package org.antop.board.post.repository

import org.antop.board.common.Pagination
import org.antop.board.common.extensions.pretty
import org.antop.board.member.model.Members
import org.antop.board.post.dto.AuthorQuery
import org.antop.board.post.dto.AuthorQueryColumn
import org.antop.board.post.dto.CommentQuery
import org.antop.board.post.dto.CommentQueryColumn
import org.antop.board.post.model.Comments
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.and
import org.springframework.stereotype.Repository

@Repository
class CommentRepository {
    /**
     * 코멘트 목록 조회
     *
     * @param postId 게시글ID
     * @param cursor 커서 기반 조회 기준 ID
     */
    fun queryCommentsByPost(
        postId: Long,
        cursor: Long = Long.MAX_VALUE,
    ): List<CommentQuery> =
        Comments
            .join(
                Members,
                JoinType.INNER,
                onColumn = Comments.authorId,
                otherColumn = Members.id,
            ).select(
                Comments.id.alias(CommentQueryColumn.COMMENT_ID),
                Comments.content.alias(CommentQueryColumn.COMMENT_CONTENT),
                Comments.created.alias(CommentQueryColumn.COMMENT_CREATED),
                Comments.modified.alias(CommentQueryColumn.COMMENT_MODIFIED),
                Members.id.alias(AuthorQueryColumn.AUTHOR_ID),
                Members.email.alias(AuthorQueryColumn.AUTHOR_EMAIL),
                Members.nickname.alias(AuthorQueryColumn.AUTHOR_NICKNAME),
            ).where {
                (Comments.post eq postId) and (Comments.removed eq false) and (Comments.id less cursor)
            }.orderBy(Comments.id to SortOrder.DESC)
            .limit(Pagination.DEFAULT_PAGE_SIZE)
            .map { row ->
                val changed =
                    row.getOrNull(Comments.created)
                        ?: row[Comments.created]
                CommentQuery(
                    id = row[Comments.id].value,
                    content = row[Comments.content],
                    author =
                        AuthorQuery(
                            id = row[Members.id].value,
                            nickname = row[Members.nickname],
                            email = row[Members.email],
                        ),
                    changed = changed.pretty(),
                )
            }
}
