package org.antop.board.post.model

import org.antop.board.common.exposed.tags
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Posts : LongIdTable(name = "posts", columnName = "post_id") {
    val subject = varchar("subject", 255)
    val content = largeText("content")
    val author = varchar("author", 100)
    val created = datetime("created")
    val modified = datetime("modified").nullable()
    val tags = tags("tags").nullable()
    val hits = long("hits").default(0L)
    val thread = long("thread")
    val depth = integer("depth").default(0)
    val removed = bool("removed").default(false)
    val comments = integer("comments").default(0)
}
