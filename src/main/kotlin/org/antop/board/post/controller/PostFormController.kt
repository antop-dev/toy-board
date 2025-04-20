package org.antop.board.post.controller

import org.antop.board.common.Pagination
import org.antop.board.common.constants.PostConstant
import org.antop.board.post.service.PostService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class PostFormController(
    private val postService: PostService,
) {
    @GetMapping(PostConstant.Url.SAVE_FORM)
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping(PostConstant.Url.EDIT_FORM)
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping(PostConstant.Url.REPLY_FORM)
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
