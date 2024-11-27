package org.antop.board.controller

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest
import org.antop.board.dto.PostEditDto
import org.antop.board.dto.PostSaveDto
import org.antop.board.service.PostService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/posts")
class PostController(
    private val postService: PostService,
) {
    @GetMapping("/list.html")
    fun list(
        model: Model,
        @RequestParam keyword: String?,
    ): String {
        val posts = postService.list(keyword)
        model.addAttribute("posts", posts)
        return "posts/list"
    }

    @GetMapping("/{id}.html")
    fun view(
        model: Model,
        @PathVariable id: Long,
    ): String {
        val post = postService.get(id)
        return post?.let {
            model.addAttribute("post", it)
            "posts/view"
        } ?: "errors/404"
    }

    @HxRequest
    @DeleteMapping("/{id}")
    fun remove(
        @PathVariable id: Long,
    ): HtmxResponse {
        postService.remove(id)
        return HtmxResponse
            .builder()
            .redirect("/posts/list.html")
            .build()
    }

    @GetMapping("/form.html")
    fun form(
        model: Model,
        @RequestParam(required = false) id: Long?,
    ): String {
        val post = id?.let { postService.get(it) }
        model.addAttribute("post", post)

        val isSave = post == null
        model.addAttribute("isSave", isSave)

        return "posts/form"
    }

    @HxRequest
    @PostMapping
    fun save(
        @RequestParam subject: String,
        @RequestParam content: String,
        @RequestParam author: String,
    ): HtmxResponse {
        val postSaveDto =
            PostSaveDto(
                subject = subject,
                content = content,
                author = author,
            )
        postService.save(postSaveDto)
        return HtmxResponse
            .builder()
            .redirect("/posts/list.html")
            .build()
    }

    @HxRequest
    @PostMapping("/{id:[0-9]+}")
    fun edit(
        @PathVariable id: Long,
        @RequestParam subject: String,
        @RequestParam content: String,
        @RequestParam author: String,
    ): HtmxResponse {
        val editDto =
            PostEditDto(
                id = id,
                subject = subject,
                content = content,
                author = author,
            )
        postService.edit(editDto)
        return HtmxResponse
            .builder()
            .redirect("/posts/$id.html")
            .build()
    }
}
