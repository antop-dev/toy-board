plugins {
    alias(libs.plugins.kotlin.jvm)
    kotlin("kapt")
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.kotlin.plugin.jpa)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.sonarqube)
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
    // jpa + querydsl
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.querydsl.jpa)
    implementation(libs.querydsl.jpa.spring)
    kapt(libs.querydsl.apt) {
        artifact {
            classifier = "jpa"
            type = "jar"
        }
    }
    kapt(libs.jakarta.annotation.api)
    kapt(libs.jakarta.persistence.api)

    annotationProcessor(libs.spring.boot.configuration.processor)
    developmentOnly(libs.spring.boot.devtools)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.kotlin.test.junit5)
    testRuntimeOnly(libs.junit.platform.launcher)
}

springBoot {
    mainClass.set("org.antop.admin.AdminApplicationKt")
}
