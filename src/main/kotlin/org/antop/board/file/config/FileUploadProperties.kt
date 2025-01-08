package org.antop.board.file.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.nio.file.Path

/**
 * 파일 업로드 관련 설정
 *
 * @property path 파일 업로드 위치
 */
@ConfigurationProperties("app.file-upload")
data class FileUploadProperties(
    val path: Path,
)
