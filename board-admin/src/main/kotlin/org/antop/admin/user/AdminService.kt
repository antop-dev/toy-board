package org.antop.admin.user

import org.antop.admin.dto.AdminDto
import org.antop.admin.jooq.tables.references.ADMINS
import org.jooq.DSLContext
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val dslContext: DSLContext,
) {
    fun findAdmin(accountName: String): AdminDto? =
        dslContext
            .selectFrom(ADMINS)
            .where(ADMINS.ACCOUNT_NAME.eq(accountName))
            .fetchOne { record ->
                AdminDto(
                    id = record.accountName,
                    password = record.password,
                    name = record.adminName,
                )
            }
}
