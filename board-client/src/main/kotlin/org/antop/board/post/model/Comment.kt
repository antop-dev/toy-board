package org.antop.board.post.model

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * 코멘트
 *
 * @property post 게시글
 * @property content 내용
 * @property created 생성일시
 * @property modified 수정일시
 * @property removed 삭제여부
 */
class Comment(
    id: EntityID<Long>,
) : LongEntity(id) {
    companion object : LongEntityClass<Comment>(Comments)

    var post by Post referencedOn Comments.post
    var authorId by Comments.authorId
    var content by Comments.content
    var created by Comments.created
    var modified by Comments.modified
    var removed by Comments.removed
        private set

    fun remove() {
        this.removed = true
    }
}
