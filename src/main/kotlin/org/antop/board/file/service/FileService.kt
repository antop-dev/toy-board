package org.antop.board.file.service

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.datetime.LocalDateTime
import org.antop.board.common.Base62
import org.antop.board.common.extensions.now
import org.antop.board.file.config.FileUploadProperties
import org.antop.board.file.dto.FileDto
import org.antop.board.file.dto.FileUploadDto
import org.antop.board.file.mapper.toDto
import org.antop.board.file.model.File
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import org.apache.tika.Tika
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID

@Service
@Transactional(readOnly = true)
class FileService(
    private val fileUploadProperties: FileUploadProperties,
) {
    private val log = KotlinLogging.logger { }

    @Transactional
    fun upload(file: FileUploadDto): FileDto {
        log.info { "uploading file: $file" }
        // 업로드된 파일 복사
        val filename = randomFilename(file.name)
        val uploadPath = fileUploadProperties.path.resolve(filename)
        val copiedSize = IOUtils.copyLarge(file.resource.inputStream, Files.newOutputStream(uploadPath))
        if (file.size != copiedSize) {
            log.warn { "file copied size ${file.size} != $copiedSize" }
            throw IOException("파일이 정상적으로 복사되지 않았습니다.")
        }
        // 디비에 저장
        val savedFile =
            File.new {
                name = file.name
                size = file.size
                type = mimeType(uploadPath)
                path = uploadPath.toString()
                created = LocalDateTime.now()
            }

        return savedFile.toDto()
    }

    @Transactional
    fun delete(base62: String) {
        val fileId = Base62.decode(base62)
        File.findById(fileId)?.let {
            log.info { "delete file. id = $fileId, path = ${it.path}" }
            Files.deleteIfExists(Path.of(it.path))
            it.delete()
        }
    }

    fun get(base62: String): FileDto? = File.findById(Base62.decode(base62))?.toDto()

    /**
     * 파일명을 랜덤하게 생성
     *
     * @param originalName 원본 파일명
     * */
    private fun randomFilename(originalName: String): String {
        val uuid = UUID.randomUUID()
        val ext = FilenameUtils.getExtension(originalName)
        return buildString {
            append(uuid)
            if (ext != null && ext.isNotBlank()) {
                append(".")
                append(ext)
            }
        }
    }

    /**
     * 파일의 타입을 추출한다
     *
     * @param path 파일 경로
     * */
    private fun mimeType(path: Path): String = Tika().detect(path.toFile())
}
