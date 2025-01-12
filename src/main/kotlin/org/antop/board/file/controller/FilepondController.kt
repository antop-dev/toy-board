package org.antop.board.file.controller

import org.antop.board.file.dto.FileUploadDto
import org.antop.board.file.service.FileService
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/filepond")
class FilepondController(
    private val fileDownloadController: FileDownloadController,
    private val fileService: FileService,
) {
    /**
     * 파일 업로드
     */
    @PostMapping
    fun process(
        @RequestParam("file") multipart: MultipartFile,
    ): String {
        val request =
            FileUploadDto(
                name = multipart.originalFilename ?: multipart.name,
                size = multipart.size,
                resource = multipart.resource,
            )
        val file = fileService.upload(request)
        return file.id
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
    ): ResponseEntity<InputStreamResource> {
        val response = fileDownloadController.download(fileId)
        return when (response.statusCode) {
            HttpStatus.OK -> { // 헤더값 하나 추가
                ResponseEntity
                    .ok()
                    .headers(response.headers)
                    .headers {
                        it["X-Content-Transfer-Id"] = fileId
                    }.body(response.body)
            }

            else -> response
        }
    }
}
