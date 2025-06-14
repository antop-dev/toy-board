package org.antop.admin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller

@ConfigurationPropertiesScan
@SpringBootApplication
@Controller
class AdminApplication

fun main(args: Array<String>) {
    runApplication<AdminApplication>(*args)
}
