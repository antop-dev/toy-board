package org.antop.board.member.model

import org.antop.board.post.model.Posts
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * 회원
 */
class Member(
    id: EntityID<Long>,
) : LongEntity(id) {
    companion object : LongEntityClass<Member>(Members)

    /** 이메일 */
    var email by Members.email

    /** 비밀번호 */
    var password by Members.password

    /** 닉네임 */
    var nickname by Members.nickname

    /** 작성일시 */
    var created by Posts.created

    /** 수정일시 */
    var modified by Posts.modified
}
