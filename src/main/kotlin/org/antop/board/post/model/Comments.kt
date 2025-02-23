package org.antop.board.post.model

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Comments : LongIdTable(name = "comments", columnName = "comment_id") {
    val content = text("content")
    val created = datetime("created")
    val modified = datetime("modified").nullable()
    val removed = bool("removed").default(false)
    val post = reference("post_id", Posts)
}
