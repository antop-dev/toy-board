package org.antop.admin.dto

data class AdminDto(
    /** 아이디 (계정명) */
    val id: String,
    /** 비밀번호 (암호화되어 있음) */
    val password: String,
    /** 관리자명 */
    val name: String,
)
