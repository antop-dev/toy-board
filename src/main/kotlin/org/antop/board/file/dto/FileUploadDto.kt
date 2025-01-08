package org.antop.board.file.dto

import org.springframework.core.io.Resource

data class FileUploadDto(
    val name: String,
    val size: Long,
    val resource: Resource,
)
