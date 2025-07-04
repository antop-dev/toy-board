package org.antop.board.member.dto

import java.io.Serializable

data class MemberDto(
    /** 회원ID */
    val id: Long,
    /** 이메일 */
    val email: String,
    /** 비밀번호 */
    var password: String,
    /** 별명 */
    val nickname: String,
    /** 프로필 이미지 */
    val avatar: String?,
) : Serializable
