package org.antop.board.file.dto

import kotlinx.datetime.LocalDateTime
import java.nio.file.Path

data class FileDto(
    val id: String,
    val name: String,
    val size: Long,
    val type: String,
    val path: Path,
    val created: LocalDateTime,
)
