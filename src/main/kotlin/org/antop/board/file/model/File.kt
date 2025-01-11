package org.antop.board.file.model

import org.antop.board.post.model.Post
import org.antop.board.post.model.PostFiles
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * 파일 엔티티
 *
 * @property name 파일명
 * @property size 파일 크기
 * @property type 파일 타입
 * @property path 파일 경로
 * @property created 생성 일시
 */
class File(
    id: EntityID<Long>,
) : LongEntity(id) {
    companion object : LongEntityClass<File>(Files)

    var name by Files.name
    var size by Files.size
    var type by Files.type
    var path by Files.path
    var created by Files.created
    var posts by Post via PostFiles
}
