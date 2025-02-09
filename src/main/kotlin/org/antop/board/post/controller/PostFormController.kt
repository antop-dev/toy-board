package org.antop.board.post.controller

import org.antop.board.common.Pagination
import org.antop.board.post.service.PostService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/posts")
class PostFormController(
    private val postService: PostService,
) {
    @GetMapping("/save.html")
    fun save(
        keyword: String?,
        paging: Pagination.Request,
        model: Model,
    ): String {
        model.addAttribute("page", paging.page)
        model.addAttribute("size", paging.size)
        model.addAttribute("keyword", keyword)
        return "posts/form/save"
    }

    @GetMapping("/edit.html")
    fun edit(
        id: Long,
        keyword: String?,
        paging: Pagination.Request,
        model: Model,
    ): String {
        val post = postService.getPost(id)
        model.addAttribute("post", post)
        model.addAttribute("page", paging.page)
        model.addAttribute("size", paging.size)
        model.addAttribute("keyword", keyword)
        return "posts/form/edit"
    }

    @GetMapping("/reply.html")
    fun reply(
        @RequestParam("parent") parentPostId: Long,
        keyword: String?,
        paging: Pagination.Request,
        model: Model,
    ): String {
        val parentPost = postService.getPost(parentPostId)
        val post =
            parentPost.copy(
                subject = "RE: ${parentPost.subject}",
                content = "",
                author = "",
                tags = setOf(),
                files = listOf(),
            )
        model.addAttribute("post", post)
        model.addAttribute("page", paging.page)
        model.addAttribute("size", paging.size)
        model.addAttribute("keyword", keyword)
        return "posts/form/reply"
    }
}
