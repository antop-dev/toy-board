package org.antop.board.file.service

import extensions.now
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import kotlinx.datetime.LocalDateTime
import org.antop.board.common.encode.Base62
import org.antop.board.file.config.FileUploadProperties
import org.antop.board.file.dto.FileDto
import org.antop.board.file.mapper.toDto
import org.antop.board.file.model.File
import org.apache.commons.io.FileUtils
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID
import kotlin.io.path.outputStream

@Service
@Transactional(readOnly = true)
class FileService(
    private val fileUploadProperties: FileUploadProperties,
) {
    private val log = KotlinLogging.logger { }

    @PostConstruct
    fun init() {
        Files.createDirectories(fileUploadProperties.path)
    }

    /**
     * 청크 파일 초기화
     */
    @Transactional(propagation = Propagation.NESTED)
    fun initChunk(request: ChunkInitRequest): String {
        // 데이터베이스에 저장하여 파일 ID를 확보한다.
        val file =
            File
                .new {
                    name = request.name
                    size = request.size
                    type = request.type
                    directory = getUploadDirectory().toAbsolutePath().toString()
                    created = LocalDateTime.now()
                }
        // 이미 디렉터리가 있다면 삭제한다.
        FileUtils.deleteDirectory(file.directoryPath.toFile())
        return Base62.encode(file.id.value)
    }

    /**
     * 청크파일 조각 업로드
     */
    @Transactional
    fun uploadChunk(request: ChunkUploadRequest) {
        val fileId = Base62.decode(request.id)
        val file = File.findById(fileId) ?: return
        if (Files.notExists(file.directoryPath)) {
            Files.createDirectories(file.directoryPath)
        }
        // 청크 조각 복사
        val chunkPath = file.directoryPath.resolve(request.offset.toString())
        request.resource.inputStream.transferTo(chunkPath.outputStream())
        // 마지막 청크 업로드
        if (request.offset + request.resource.contentLength() >= file.size) {
            file.complete()
        }
    }

    /**
     * 파일 삭제
     */
    @Transactional
    fun delete(base62: String) {
        val fileId = Base62.decode(base62)
        File.findById(fileId)?.let { file ->
            FileUtils.deleteDirectory(file.directoryPath.toFile())
            log.debug { "Deleted chunk directory ${file.directoryPath}" }
            file.delete()
        }
    }

    /**
     * 파일 정보 조회
     */
    fun getFile(base62: String): FileDto {
        val file = File.findById(Base62.decode(base62)) ?: throw FileNotFoundException(base62)
        if (!file.completed || Files.notExists(file.directoryPath) || !file.checkSize()) {
            throw FileNotFoundException(file.directory)
        }
        return file.toDto()
    }

    /**
     * 파일 아이디에 해당하는 청크 파일이 업로드될 디렉터리 위치를 리턴
     */
    private fun getUploadDirectory(): Path {
        val uuid = UUID.randomUUID().toString()
        val uploadPath = fileUploadProperties.path.resolve(uuid)
        FileUtils.deleteDirectory(uploadPath.toFile())
        Files.createDirectories(uploadPath)
        return uploadPath
    }

    data class ChunkInitRequest(
        val name: String,
        val type: String,
        val size: Long,
    )

    data class ChunkUploadRequest(
        val id: String,
        val offset: Long,
        val resource: Resource,
    )
}
