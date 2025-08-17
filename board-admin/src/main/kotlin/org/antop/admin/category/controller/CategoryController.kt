package org.antop.admin.category.controller

import org.antop.admin.category.dto.CategorySaveDto
import org.antop.admin.category.service.CategoryService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/category")
class CategoryController(
    private val categoryService: CategoryService,
) {
    @GetMapping(path = ["", "/list.html"])
    fun list(
        model: Model,
        @RequestParam(name = "name", required = false) name: String?,
        @RequestParam(name = "status", required = false) statusCode: String?,
    ): String {
        val categories = categoryService.getList(name, statusCode)
        model.addAttribute("categories", categories)

        return "category/list"
    }

    @GetMapping("/form.html")
    fun form(
        model: Model,
        @RequestParam(name = "id", required = false) categoryId: Long?,
    ): String {
        if (categoryId != null) {
            val category = categoryService.getCategory(categoryId)
            model.addAttribute("category", category)
        }
        return "category/form"
    }

    @PostMapping("/submit")
    fun submit(
        @RequestParam(name = "id", required = false) categoryId: Long?,
        @RequestParam(name = "name") name: String,
        @RequestParam(name = "status") statusCode: String,
        @RequestParam(name = "featureWrite", required = false) featureWrite: Boolean = false,
        @RequestParam(name = "featureSecret", required = false) featureSecret: Boolean = false,
        @RequestParam(name = "featureFile", required = false) featureFile: Boolean = false,
        @RequestParam(name = "featureReply", required = false) featureReply: Boolean = false,
        @RequestParam(name = "featureComment", required = false) featureComment: Boolean = false,
        @RequestParam(name = "featureVote", required = false) featureVote: Boolean = false,
        @RequestParam(name = "featureShare", required = false) featureShare: Boolean = false,
    ): String {
        val category =
            CategorySaveDto(
                name = name,
                statusCode = statusCode,
                featureWrite = featureWrite,
                featureSecret = featureSecret,
                featureFile = featureFile,
                featureReply = featureReply,
                featureComment = featureComment,
                featureVote = featureVote,
                featureShare = featureShare,
            )

        if (categoryId == null) { // 등록
            categoryService.register(category)
        } else { // 수정
            categoryService.modify(categoryId, category)
        }
        return "redirect:/category/list.html"
    }
}
