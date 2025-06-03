package org.antop.board

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@ConfigurationPropertiesScan
@SpringBootApplication
@Controller
class BoardApplication {
    @RequestMapping("/")
    fun index(): String = "redirect:/posts/list.html"
}

fun main(args: Array<String>) {
    runApplication<BoardApplication>(*args)
}
