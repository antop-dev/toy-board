package org.antop.board.member.model

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Members : LongIdTable(name = "users", columnName = "user_id") {
    val email = varchar("email", 100)
    val password = varchar("password", 255)
    val nickname = varchar("nickname", 100)
    val avatar = blob("avatar").nullable()
    val created = datetime("created")
    val modified = datetime("modified").nullable()
}
