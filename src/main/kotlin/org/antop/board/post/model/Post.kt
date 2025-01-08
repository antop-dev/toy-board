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
    var tags: Set<String> by Posts.tags
    var hits by Posts.hits
    var files by File via PostFiles
}
