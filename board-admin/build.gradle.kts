plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.jte.gradle)
    alias(libs.plugins.jooq)
}

dependencies {
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.aop)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.security)

    implementation(libs.slf4j.api)
    implementation(libs.jul.to.slf4j)
    implementation(libs.jcl.over.slf4j)

    implementation(libs.spring.boot.starter.jooq)
    implementation(libs.jooq)

    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.prettytime)
    implementation(libs.kotlin.logging.jvm)
    implementation(libs.mariadb.java.client)
    implementation(libs.p6spy.spring.boot.starter)
    implementation(libs.sql.formatter)

    implementation(libs.jte.spring.boot.starter)
    implementation(libs.jte.kotlin)
    jteGenerate(libs.jte.models)

    jooqGenerator(libs.mariadb.java.client)
    jooqGenerator(libs.jooq)
    jooqGenerator(libs.jooq.meta)
    jooqGenerator(libs.jooq.codegen)
    jooqGenerator(libs.slf4j.jdk14)

    annotationProcessor(libs.spring.boot.configuration.processor)
    developmentOnly(libs.spring.boot.devtools)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.kotlin.test.junit5)
    testRuntimeOnly(libs.junit.platform.launcher)
}

jte {
    generate()
    binaryStaticContent.set(true)
    sourceDirectory.set(project.file("src/main/resources/templates").toPath())
}

jooq {
    version.set(libs.versions.jooq)
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)

    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN

                jdbc.apply {
                    driver = "org.mariadb.jdbc.Driver"
                    url = "jdbc:mariadb://localhost:3306/toy_board"
                    user = "antop"
                    password = "local"
                }

                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator" // Kotlin 코드 생성
                    database.apply {
                        name = "org.jooq.meta.mariadb.MariaDBDatabase"
                        inputSchema = "toy_board"
                        includes = ".*" // 모든 테이블 포함
                    }

                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = false
                        isFluentSetters = true
                        isJavaTimeTypes = true
                        isKotlinNotNullPojoAttributes = true
                        isKotlinNotNullRecordAttributes = true
                    }

                    target.apply {
                        packageName = "org.antop.admin.jooq" // 생성될 코드의 패키지명
                        directory = "build/generated-sources/jooq" // 생성될 코드의 위치
                    }
                }
            }
        }
    }
}

// jOOQ 코드 생성 작업이 컴파일 전에 실행되도록 설정
tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") {
    allInputsDeclared.set(true)
}
