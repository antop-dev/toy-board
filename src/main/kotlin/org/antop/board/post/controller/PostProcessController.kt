package org.antop.board.post.controller

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest
import org.antop.board.post.dto.PostDto
import org.antop.board.post.service.PostSaveServiceRequest
import org.antop.board.post.service.PostService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/posts")
class PostProcessController(
    private val postService: PostService,
) {
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

    @HxRequest
    @PostMapping("/save")
    fun save(
        @RequestParam subject: String,
        @RequestParam content: String,
        @RequestParam author: String,
        @RequestParam(required = false) tags: Set<String>?,
        @RequestParam("file") files: List<String> = listOf(),
        @RequestParam("parent", required = false) parentPostId: Long?,
    ): HtmxResponse {
        val postSaveDto = buildPostSaveServiceRequest(subject, content, author, tags, files)
        val post = postService.save(postSaveDto)
        return buildViewHtmxResponse(post)
    }

    @HxRequest
    @PostMapping("/reply")
    fun reply(
        @RequestParam("parent") parentPostId: Long,
        @RequestParam subject: String,
        @RequestParam content: String,
        @RequestParam author: String,
        @RequestParam(required = false) tags: Set<String>?,
        @RequestParam("file") files: List<String> = listOf(),
    ): HtmxResponse {
        val postSaveDto = buildPostSaveServiceRequest(subject, content, author, tags, files)
        val post = postService.reply(parentPostId, postSaveDto)
        return buildViewHtmxResponse(post)
    }

    @HxRequest
    @PostMapping("/edit")
    fun edit(
        @RequestParam id: Long,
        @RequestParam subject: String,
        @RequestParam content: String,
        @RequestParam author: String,
        @RequestParam(required = false) tags: Set<String>?,
        @RequestParam("file") files: List<String> = listOf(),
    ): HtmxResponse {
        val saveRequest = buildPostSaveServiceRequest(subject, content, author, tags, files)
        val post = postService.edit(id, saveRequest)
        return buildViewHtmxResponse(post)
    }

    private fun buildViewHtmxResponse(post: PostDto) =
        HtmxResponse
            .builder()
            .redirect("/posts/view.html?id=${post.id}")
            .build()

    private fun buildPostSaveServiceRequest(
        subject: String,
        content: String,
        author: String,
        tags: Set<String>?,
        files: List<String>,
    ) = PostSaveServiceRequest(
        subject = subject,
        content = content,
        author = author,
        tags = tags,
        files = files.filter { it.isNotBlank() },
    )
}
