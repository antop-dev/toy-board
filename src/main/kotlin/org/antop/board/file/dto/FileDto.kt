package org.antop.board.file.dto

import kotlinx.datetime.LocalDateTime

data class FileDto(
    val id: String,
    val name: String,
    val size: Long,
    val type: String,
    val path: String,
    val created: LocalDateTime,
)
