package org.antop.admin.category

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/category")
class CategoryController {
    @GetMapping
    fun index(): String = "category/index"
}
