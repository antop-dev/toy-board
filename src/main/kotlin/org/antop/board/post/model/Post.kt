package org.antop.board.post.model

import org.antop.board.file.model.File
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * 게시글
 */
class Post(
    id: EntityID<Long>,
) : LongEntity(id) {
    companion object : LongEntityClass<Post>(Posts)

    /** 제목 */
    var subject by Posts.subject

    /** 내용 */
    var content by Posts.content

    /** 작성자 */
    var authorId by Posts.authorId

    /** 작성일시 */
    var created by Posts.created

    /** 마지막 수정일시 */
    var modified by Posts.modified

    /** 테크 목록 */
    var tags: Set<String>? by Posts.tags

    /** 조회 수 */
    var hits by Posts.hits

    /** 첨부파일 목록 */
    var files by File via PostFiles

    /** 답글 쓰레드 */
    var thread: Long by Posts.thread

    /** 답글 들여쓰기 */
    var depth: Int by Posts.depth

    /** 삭제 여부 (소프트 삭제) */
    var removed by Posts.removed
        private set

    /** 댓글(Comment) 수 */
    var comments by Posts.comments

    /** 좋아요 수 */
    var likes by Posts.likes

    /** 싫어요 수 */
    var dislikes by Posts.dislikes

    /** 비밀글 여부 */
    var secret by Posts.secret

    /** 상위 게시글ID */
    var parentId by Posts.parentId

    /**
     * 삭제 처리 (소프트 삭제)
     */
    fun remove() {
        this.removed = true
    }

    /**
     * 해당 게시글이 가질 수 있는 최소 스레드 값
     */
    fun minThread(): Long {
        val min =
            if (thread % 1000 == 0L) { // 메인글 (1000, 2000, 3000)
                thread - 1000
            } else { // 쓰레드 (982, 1999, 2100)
                thread / 1000 * 1000
            }
        return min + 1
    }
}
