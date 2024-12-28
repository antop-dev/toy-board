package org.antop.board.model

import org.antop.board.common.exposed.jsonArray
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Posts : LongIdTable(name = "posts", columnName = "post_id") {
    val subject = varchar("subject", 255)
    val content = text("content")
    val author = varchar("author", 100)
    val createdAt = datetime("created")
    val modifiedAt = datetime("modified").nullable()
    val tags = jsonArray("tags")
}
