package org.antop.board.file.model

import extensions.now
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.datetime.LocalDateTime
import org.antop.board.post.model.Post
import org.antop.board.post.model.PostFiles
import org.apache.tika.Tika
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.nio.file.Path
import kotlin.io.path.fileSize
import kotlin.io.path.isRegularFile
import kotlin.streams.asSequence

/**
 * 파일 엔티티
 *
 * @property name 파일명
 * @property size 파일 크기
 * @property type 파일 타입
 * @property directory 업로드된 디렉터리 (String)
 * @property completed 업로드 완료 여부
 * @property created 생성 일시
 * @property modified 수정 일시
 * @property posts 업로드된 파일을 사용하는 게시물 목록
 * @property directoryPath 업로드된 디렉터리 (Path)
 */
class File(
    id: EntityID<Long>,
) : LongEntity(id) {
    companion object : LongEntityClass<File>(Files) {
        private val log = KotlinLogging.logger {}
    }

    var name by Files.name
    var size by Files.size
    var type by Files.type
    var directory by Files.directory
    var completed by Files.completed
        private set
    var created by Files.created

    // TODO: https://jetbrains.github.io/Exposed/dao-crud-operations.html#auto-fill-columns-on-entity-change
    var modified by Files.modified
    var posts by Post via PostFiles

    val directoryPath: Path
        get() = Path.of(directory)

    fun complete() {
        try {
            // 첫번째 파일에서 타입을 알아냄
            this.type = Tika().detect(directoryPath.resolve("0").toFile())
        } catch (e: Exception) {
            log.warn { "Content type analysis filed : ${e.message}" }
        }
        this.completed = checkSize()
        this.modified = LocalDateTime.now()
    }

    /**
     * 실제 업로드된 파일들의 용량을 확인한다.
     */
    fun checkSize(): Boolean {
        val totalSize =
            java.nio.file.Files
                .walk(Path.of(directory))
                .filter { it.isRegularFile() }
                .map { it.fileSize() }
                .asSequence()
                .sumOf { it }
        return this.size == totalSize
    }
}
