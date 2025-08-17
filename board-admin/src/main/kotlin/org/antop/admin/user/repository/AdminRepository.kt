package org.antop.admin.user.repository

import org.antop.admin.user.model.Admin
import org.springframework.data.jpa.repository.JpaRepository

interface AdminRepository : JpaRepository<Admin, Long> {
    /**
     * 계정명으로 관리자 정보 조회
     *
     * @param account 계정명
     * @return 관리자 정보
     */
    fun findByAccount(account: String): Admin?
}
