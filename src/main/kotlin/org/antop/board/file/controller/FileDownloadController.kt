package org.antop.board.file.controller

import org.antop.board.file.service.FileService
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ContentDisposition
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.nio.file.Files
import java.nio.file.Path

/**
 * 파일 다운로드
 */
@Controller
@RequestMapping("/files")
class FileDownloadController(
    private val fileService: FileService,
) {
    /**
     * 파일 다운로드
     */
    @GetMapping("/{fileId:[0-9a-zA-Z]+}")
    fun download(
        @PathVariable fileId: String,
    ): ResponseEntity<InputStreamResource> {
        return fileService
            .get(fileId)
            ?.takeIf { file -> Files.exists(Path.of(file.path)) }
            ?.let { file ->
                val resource = InputStreamResource(Files.newInputStream(Path.of(file.path)))
                return ResponseEntity
                    .ok()
                    .headers {
                        it.pragma = "no-cache"
                        it.expires = -1
                        it.contentLength = file.size
                        it.contentType = MediaType.parseMediaType(file.type)
                        it.contentDisposition = contentDisposition(file.name)
                    }.body(resource)
            }
            ?: ResponseEntity.notFound().build()
    }

    private fun contentDisposition(filename: String) =
        ContentDisposition
            .attachment()
            .filename(filename, Charsets.UTF_8)
            .build()
}
