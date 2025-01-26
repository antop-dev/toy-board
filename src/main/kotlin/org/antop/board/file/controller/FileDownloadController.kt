package org.antop.board.file.controller

import org.antop.board.file.common.ChunkInputStreamResource
import org.antop.board.file.service.FileService
import org.springframework.core.io.Resource
import org.springframework.http.ContentDisposition
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.io.FileNotFoundException

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
    ): ResponseEntity<Resource> =
        try {
            val file = fileService.getFile(fileId)
            val resource = ChunkInputStreamResource(file.path)
            ResponseEntity
                .ok()
                .headers {
                    it.pragma = "no-cache"
                    it.expires = -1
                    it.contentLength = file.size
                    it.contentType = MediaType.parseMediaType(file.type)
                    it.contentDisposition = ContentDisposition.attachment().filename(file.name, Charsets.UTF_8).build()
                }.body(resource)
        } catch (e: FileNotFoundException) {
            ResponseEntity.notFound().build()
        }
}
