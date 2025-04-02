package org.antop.board.post.controller

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest
import org.antop.board.captcha.ValidCaptcha
import org.antop.board.common.constants.PostConsts
import org.antop.board.login.UserPrincipal
import org.antop.board.member.dto.MemberDto
import org.antop.board.post.dto.PostDto
import org.antop.board.post.service.PostSaveServiceRequest
import org.antop.board.post.service.PostService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class PostProcessController(
    private val postService: PostService,
) {
    @HxRequest
    @DeleteMapping(PostConsts.Url.PREFIX + "/{id}")
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
    @ValidCaptcha
    @PostMapping(PostConsts.Url.SAVE_PROCESS)
    @PreAuthorize("isAuthenticated()")
    fun save(
        @RequestParam subject: String,
        @RequestParam content: String,
        @RequestParam(required = false) tags: Set<String>?,
        @RequestParam("file") files: List<String> = listOf(),
        @AuthenticationPrincipal principal: UserPrincipal,
    ): HtmxResponse {
        val postSaveServiceRequest = buildPostSaveServiceRequest(subject, content, principal.member, tags, files)
        val post = postService.save(postSaveServiceRequest)
        return buildViewHtmxResponse(post)
    }

    @HxRequest
    @ValidCaptcha
    @PostMapping(PostConsts.Url.REPLY_PROCESS)
    fun reply(
        @RequestParam("parent") parentPostId: Long,
        @RequestParam subject: String,
        @RequestParam content: String,
        @RequestParam(required = false) tags: Set<String>?,
        @RequestParam("file") files: List<String> = listOf(),
        @AuthenticationPrincipal principal: UserPrincipal,
    ): HtmxResponse {
        val postSaveDto = buildPostSaveServiceRequest(subject, content, principal.member, tags, files)
        val post = postService.reply(parentPostId, postSaveDto)
        return buildViewHtmxResponse(post)
    }

    @HxRequest
    @ValidCaptcha
    @PostMapping(PostConsts.Url.EDIT_PROCESS)
    fun edit(
        @RequestParam id: Long,
        @RequestParam subject: String,
        @RequestParam content: String,
        @RequestParam(required = false) tags: Set<String>?,
        @RequestParam("file") files: List<String> = listOf(),
        @AuthenticationPrincipal principal: UserPrincipal,
    ): HtmxResponse {
        val saveRequest = buildPostSaveServiceRequest(subject, content, principal.member, tags, files)
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
        author: MemberDto,
        tags: Set<String>?,
        files: List<String>,
    ) = PostSaveServiceRequest(
        subject = subject,
        content = content,
        authorId = author.id,
        tags = tags,
        files = files.filter { it.isNotBlank() },
    )
}
