package org.antop.board.file.common

import org.springframework.core.io.AbstractResource
import org.springframework.core.io.PathResource
import org.springframework.core.io.Resource
import java.io.SequenceInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.Collections
import kotlin.io.path.isRegularFile

/**
 * 나눠진 업로드된 파일을 한번에 읽어들이는 리소스
 */
class ChunkInputStreamResource(
    private val directory: Path,
) : AbstractResource() {
    private val resources: List<Resource> =
        Files
            .walk(directory)
            .filter { it.isRegularFile() } // 파일만
            .sorted(Comparator.comparingLong { it.fileName.toString().toLong() }) // 오프셋 순으로 정렬
            .map { PathResource(it) }
            .toList()

    override fun getInputStream() = SequenceInputStream(Collections.enumeration(resources.map { it.inputStream }))

    override fun getDescription(): String = "chunk directory [${directory.toAbsolutePath()}]"
}
