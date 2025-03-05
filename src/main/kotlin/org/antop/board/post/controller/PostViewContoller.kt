package org.antop.board.post.controller

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxReswap
import jakarta.servlet.http.HttpServletRequest
import org.antop.board.common.Pagination
import org.antop.board.common.extensions.comma
import org.antop.board.post.service.CommentService
import org.antop.board.post.service.PostHitsService
import org.antop.board.post.service.PostService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/posts")
class PostViewContoller(
    private val postService: PostService,
    private val postHitsService: PostHitsService,
    private val commentService: CommentService,
) {
    @GetMapping("/list.html")
    fun list(
        model: Model,
        paging: Pagination.Request,
        @RequestParam keyword: String?,
    ): String {
        val resp = postService.getPosts(keyword, paging.page, paging.size)

        val pagination =
            Pagination.Ui(
                items = resp.items,
                page = paging.page,
                size = paging.size,
                total = resp.total,
            )

        model.addAttribute("keyword", keyword)
        model.addAttribute("pagination", pagination)
        return "posts/list"
    }

    @GetMapping("/view.html")
    fun view(
        model: Model,
        @RequestParam id: Long,
        keyword: String?,
        paging: Pagination.Request,
        request: HttpServletRequest,
    ): String {
        // 게시글 조회
        val post = postService.getPost(id)
        // 조회수 증가
        val visitorId = request.remoteAddr
        val hits = postHitsService.incrementHits(post, visitorId)
        // 코멘트 목록
        val comments = commentService.getComments(post.id)

        model.addAttribute("post", post.copy(hits = hits))
        model.addAttribute("page", paging.page)
        model.addAttribute("size", paging.size)
        model.addAttribute("keyword", keyword)
        model.addAttribute("comments", comments)
        return "posts/view/view"
    }

    @HxRequest
    @PostMapping("{postId}/like")
    @ResponseBody
    @HxReswap
    fun like(
        @PathVariable postId: Long,
    ): String {
        val post = postService.like(postId)
        return (post?.likes ?: 0).comma()
    }

    @HxRequest
    @PostMapping("{postId}/dislike")
    @ResponseBody
    @HxReswap
    fun dislike(
        @PathVariable postId: Long,
    ): String {
        val post = postService.dislike(postId)
        return (post?.dislikes ?: 0).comma()
    }
}
