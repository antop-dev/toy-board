package org.antop.admin

import io.github.openfeign.querydsl.jpa.spring.repository.config.EnableQuerydslRepositories
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@EnableQuerydslRepositories
@ConfigurationPropertiesScan
@SpringBootApplication
class AdminApplication

fun main(args: Array<String>) {
    runApplication<AdminApplication>(*args)
}
