package org.antop.board.model

import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Post(
    id: EntityID<Long>,
) : LongEntity(id) {
    companion object : LongEntityClass<Post>(Posts)

    /** 제목 */
    var subject by Posts.subject

    /** 내용 */
    var content by Posts.content

    /** 작성자 */
    var author by Posts.author

    /** 등록일시 */
    var createdAt by Posts.createdAt

    /** 마지막 수정일시 */
    var modifiedAt by Posts.modifiedAt

    /** 마지막 변경일시 */
    val changeAt: LocalDateTime
        get() = modifiedAt ?: createdAt

    /** 태그 */
    var tags: Set<String> by Posts.tags
}
