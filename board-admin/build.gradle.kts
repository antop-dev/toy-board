import org.jooq.meta.jaxb.*

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.sonarqube)
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

    implementation(libs.spring.boot.starter.thymeleaf)
    implementation(libs.thymeleaf.layout.dialect)
    implementation(libs.thymeleaf.extras.springsecurity)

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

                        forcedTypes.addAll(
                            listOf(
                                ForcedType().apply {
                                    name = "Boolean"
                                    includeTypes = "TINYINT\\(1\\)"
                                    includeExpression = ".*"
                                    nullability = Nullability.NOT_NULL
                                },
                                ForcedType().apply {
                                    name = "org.antop.admin.category.CategoryStatus"
                                    includeExpression = "categories\\.status_code"
                                    includeTypes = "CHAR"
                                    userType = "org.antop.admin.category.CategoryStatus"
                                    converter = "org.antop.admin.common.jooq.CategoryStatusConverter"
                                    nullability = Nullability.NOT_NULL
                                },
                            ),
                        )
                    }

                    generate.apply {
                        isPojosAsKotlinDataClasses = false
                        isKotlinNotNullRecordAttributes = true
                        isKotlinDefaultedNullableRecordAttributes = false
                        isKotlinNotNullInterfaceAttributes = true
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = false
                        isFluentSetters = false
                        isJavaTimeTypes = true
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

tasks.register<Delete>("cleanJooq") {
    group = "jOOQ"
    description = "jOOQ에서 생성된 코드를 삭제"
    delete(
        layout.buildDirectory.dir("generated-sources/jooq"),
    )
}

// jOOQ 코드 생성 작업이 컴파일 전에 실행되도록 설정
tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") {
    allInputsDeclared.set(true)
    dependsOn("cleanJooq") // cleanJooq task를 먼저 실행하도록 의존성 추가
}

springBoot {
    mainClass.set("org.antop.admin.AdminApplicationKt")
}
