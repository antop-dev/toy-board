package org.antop.board.file.model

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Files : LongIdTable(name = "files", columnName = "file_id") {
    val name = varchar("file_name", 255)
    val size = long("file_size")
    val type = varchar("file_type", 100)
    val path = varchar("file_path", 255)
    val created = datetime("created")
}
