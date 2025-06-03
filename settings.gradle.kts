plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "toy-board"
// 사용자
include("board-client")
// 관리자 (백오피스)
include("board-admin")
