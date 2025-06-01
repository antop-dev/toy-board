package org.antop.board.post.model

import org.antop.board.file.model.Files
import org.jetbrains.exposed.sql.Table

/**
 * 테이블 구조상 N:M 이지만 1:N으로 사용
 */
object PostFiles : Table("post_files") {
    val post = reference("post_id", Posts)
    val file = reference("file_id", Files)
    override val primaryKey = PrimaryKey(post, file)
}
