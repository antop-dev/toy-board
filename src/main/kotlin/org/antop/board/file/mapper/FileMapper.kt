package org.antop.board.file.mapper

import org.antop.board.common.Base62
import org.antop.board.file.dto.FileDto
import org.antop.board.file.model.File

fun File.toDto() =
    FileDto(
        id = Base62.encode(id.value),
        name = name,
        size = size,
        type = type,
        path = path,
        created = created,
    )
