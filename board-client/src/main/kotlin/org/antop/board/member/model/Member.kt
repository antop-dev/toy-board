package org.antop.board.member.model

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

    /** 프로핑 이미지 */
    var avatar by Members.avatar

    /** 작성일시 */
    var created by Members.created

    /** 수정일시 */
    var modified by Members.modified
}
