package org.antop.admin.user.service

import org.antop.admin.user.dto.AdminDto
import org.antop.admin.user.repository.AdminRepository
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val adminRepository: AdminRepository,
) {
    fun findAdmin(accountName: String): AdminDto? =
        adminRepository.findByAccount(accountName)?.let {
            AdminDto(
                id = it.id,
                username = it.account,
                password = it.password,
                name = it.name,
            )
        }
}
