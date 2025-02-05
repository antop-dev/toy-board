package org.antop.board.post.model

import org.antop.board.file.model.File
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * 게시글
 *
 * @property id 게시글 ID
 * @property subject 제목
 * @property content 내용
 * @property author 작성자
 * @property created 등록일시
 * @property modified 마지막 수정일시
 * @property tags 태그
 * @property hits 조회수
 * @property files 첨부파일 목록
 * @property thread 쓰레드
 * @property depth 들여쓰기
 */
class Post(
    id: EntityID<Long>,
) : LongEntity(id) {
    companion object : LongEntityClass<Post>(Posts)

    var subject by Posts.subject
    var content by Posts.content
    var author by Posts.author
    var created by Posts.created
    var modified by Posts.modified
    var tags: Set<String>? by Posts.tags
    var hits by Posts.hits
    var files by File via PostFiles
    var thread: Long by Posts.thread
    var depth: Int by Posts.depth
    var removed by Posts.removed
        private set

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
