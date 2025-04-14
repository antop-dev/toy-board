package org.antop.board.post.controller

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest
import org.antop.board.login.UserPrincipal
import org.antop.board.post.dto.CommentDto
import org.antop.board.post.service.CommentService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.View
import org.springframework.web.servlet.view.FragmentsRendering

@RestController
class CommentController(
    private val commentService: CommentService,
) {
    @HxRequest
    @GetMapping("/posts/{postId:[0-9]+}/comments")
    fun getComments(
        @PathVariable postId: Long,
        @RequestParam(required = false) before: Long = Long.MAX_VALUE,
        model: Model,
    ): View {
        val comments = commentService.getComments(postId, before)
        model.addAttribute("comments", comments)
        model.addAttribute("postId", postId)

        return FragmentsRendering
            .with("posts/view/fragments :: comments")
            .build()
    }

    @HxRequest
    @PostMapping("/posts/{postId:[0-9]+}/comments")
    fun save(
        @PathVariable postId: Long,
        @RequestParam content: String,
        @AuthenticationPrincipal principal: UserPrincipal,
        model: Model,
    ): View {
        val comment = commentService.save(postId, content, principal.id)
        model.addAttribute("c", comment)

        return FragmentsRendering
            .with("posts/view/fragments :: comment")
            .build()
    }

    @HxRequest
    @PutMapping("/comments/{commentId:[0-9]+}")
    fun edit(
        @PathVariable commentId: Long,
        @RequestParam content: String,
        @AuthenticationPrincipal principal: UserPrincipal,
    ): CommentDto = commentService.save(commentId, content, principal.id)

    @HxRequest
    @DeleteMapping("/comments/{commentId:[0-9]+}")
    fun delete(
        @PathVariable commentId: Long,
    ) {
        commentService.remove(commentId)
    }
}
