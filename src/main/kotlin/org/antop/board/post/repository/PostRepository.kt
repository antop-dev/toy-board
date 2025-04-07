package org.antop.board.post.repository

import org.antop.board.post.dto.PostQuery
import org.antop.board.post.model.Post

interface PostRepository {
    /**
     * [id]로 게시글 한건 조회
     */
    fun findPost(id: Long): Post?

    /**
     * 게시글 목록 조회
     */
    fun queryPosts(
        keyword: String?,
        page: Long,
        pageSize: Int,
    ): List<PostQuery>

    /**
     * 게시글 카운트 조회
     */
    fun count(keyword: String?): Long
}
