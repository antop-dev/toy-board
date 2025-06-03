package org.antop.board.file.controller

import org.antop.board.file.common.FilepondHttpHeaders
import org.antop.board.file.service.FileService
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@RestController
@RequestMapping("/filepond")
class FilepondController(
    private val fileDownloadController: FileDownloadController,
    private val fileService: FileService,
) {
    /**
     * 청크 업로드 초기화
     */
    @PostMapping
    fun process(
        @RequestHeader(FilepondHttpHeaders.UPLOAD_NAME) encodedFilename: String,
        @RequestHeader(FilepondHttpHeaders.UPLOAD_LENGTH) size: Long,
        @RequestHeader(FilepondHttpHeaders.UPLOAD_TYPE) contentType: String?,
    ): String {
        val request =
            FileService.ChunkInitRequest(
                name = URLDecoder.decode(encodedFilename, StandardCharsets.UTF_8),
                type = contentType ?: MediaType.APPLICATION_OCTET_STREAM_VALUE,
                size = size,
            )
        return fileService.initChunk(request)
    }

    /**
     * 청크 업로드
     */
    @PatchMapping
    fun patch(
        @RequestParam("patch") fileId: String,
        @RequestHeader(FilepondHttpHeaders.UPLOAD_OFFSET) offset: Long,
        @RequestBody payload: Resource,
    ) {
        val request =
            FileService.ChunkUploadRequest(
                id = fileId,
                offset = offset,
                resource = payload,
            )
        fileService.uploadChunk(request)
    }

    /**
     * 업로드한 파일 석제
     */
    @DeleteMapping
    fun revert(
        @RequestBody fileId: String,
    ) {
        fileService.delete(fileId)
    }

    /**
     * Filepond로 업로드 해놓은 파일 불러오기
     */
    @GetMapping
    fun load(
        @RequestParam("load") fileId: String,
    ): ResponseEntity<Resource> {
        val response = fileDownloadController.download(fileId)
        return when (response.statusCode) {
            HttpStatus.OK -> { // 헤더값 하나 추가
                ResponseEntity
                    .ok()
                    .headers(response.headers)
                    .headers {
                        it[FilepondHttpHeaders.TRANSFER_ID] = fileId
                    }.body(response.body)
            }

            else -> response
        }
    }
}
