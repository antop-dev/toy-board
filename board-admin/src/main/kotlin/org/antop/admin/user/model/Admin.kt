package org.antop.admin.user.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "admins")
class Admin(
    /** 계정명 (로그인) */
    @Column(name = "account_name")
    var account: String,
    /** 비밀번호 */
    @Column(name = "password")
    var password: String,
    /** 관리자명 */
    @Column(name = "admin_name")
    var name: String,
) {
    @Id
    @Column(name = "admin_Id")
    val id: Long = 0
}
