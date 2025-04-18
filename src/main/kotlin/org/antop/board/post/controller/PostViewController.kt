package org.antop.board.post.controller

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxReswap
import jakarta.servlet.http.HttpServletRequest
import org.antop.board.common.Pagination
import org.antop.board.common.constants.LoginConsts
import org.antop.board.common.extensions.comma
import org.antop.board.member.service.MemberService
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
class PostViewController(
    private val postService: PostService,
    private val postHitsService: PostHitsService,
    private val memberService: MemberService,
) {
    @GetMapping(path = ["/", "/list.html"])
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
        @RequestParam keyword: String?,
        paging: Pagination.Request,
        request: HttpServletRequest,
    ): String {
        // 게시글 조회
        val post = postService.getPost(id)
        // 조회수 증가
        val visitorId = request.remoteAddr
        val hits = postHitsService.incrementHits(post, visitorId)
        // 작성자 조회
        val author = memberService.getMember(post.authorId)

        model.addAttribute("loginUrl", LoginConsts.Url.LOGIN_FORM)
        model.addAttribute("post", post.copy(hits = hits))
        model.addAttribute("page", paging.page)
        model.addAttribute("size", paging.size)
        model.addAttribute("keyword", keyword)
        model.addAttribute("author", author)
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
        return post.likes.comma()
    }

    @HxRequest
    @PostMapping("{postId}/dislike")
    @ResponseBody
    @HxReswap
    fun dislike(
        @PathVariable postId: Long,
    ): String {
        val post = postService.dislike(postId)
        return post.dislikes.comma()
    }
}
