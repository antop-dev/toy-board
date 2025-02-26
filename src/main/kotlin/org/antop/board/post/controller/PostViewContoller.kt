package org.antop.board.post.controller

import jakarta.servlet.http.HttpServletRequest
import org.antop.board.common.Pagination
import org.antop.board.post.service.CommentService
import org.antop.board.post.service.PostHitsService
import org.antop.board.post.service.PostService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

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
}
