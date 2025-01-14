package org.antop.board.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.h2.tools.Server
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.isDirectory

@Profile("local")
@Configuration
@ConditionalOnClass(DataSourceAutoConfiguration::class)
@AutoConfigureBefore(DataSourceAutoConfiguration::class)
class LocalH2ServerConfig(
    private val localH2Properties: LocalH2Properties,
) {
    private val log = KotlinLogging.logger { }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    fun h2(): Server {
        if (localH2Properties.init) {
            removeOldDatabaseFiles()
        }
        return Server
            .createTcpServer(
                "-tcp",
                "-ifNotExists", // 데이터베이스가 없으면 새로 생성 허용
                "-tcpAllowOthers", // 원격 접속 허용 (선택 사항)
                "-tcpPort",
                localH2Properties.port.toString(),
            ).apply {
                log.info { "H2 local server = ${this.url}" }
            }
    }

    /**
     * 서버 띄우기 전에 이미 있던 데이터베이스 파일을 삭제한다
     */
    private fun removeOldDatabaseFiles() {
        val path = Path.of(localH2Properties.database)
        val directory = directory(path.parent)
        val prefix = path.fileName.toString()

        Files.list(directory).forEach {
            if (it.isDirectory()) return@forEach
            val filename = it.fileName.toString()
            if (filename.startsWith(prefix) && filename.endsWith(".db")) {
                log.info { "Remove old database file = $it" }
                Files.delete(it)
            }
        }
    }

    private fun directory(path: Path): Path =
        when (path.toString()) {
            "~" -> Path.of(System.getProperty("user.home"))
            else -> path
        }
}
