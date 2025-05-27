package org.antop.board.feed

import com.rometools.rome.feed.rss.Category
import com.rometools.rome.feed.rss.Channel
import com.rometools.rome.feed.rss.Item
import extensions.now
import extensions.toJavaDate
import jakarta.servlet.http.HttpServletRequest
import kotlinx.datetime.LocalDateTime
import org.antop.board.post.dto.PostQuery
import org.antop.board.post.service.PostService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RssController(
    private val postService: PostService,
) : FeedHelper() {
    @GetMapping("/feed/rss", produces = [MediaType.APPLICATION_RSS_XML_VALUE])
    fun rss(request: HttpServletRequest): Channel {
        val posts = postService.getPosts()
        return Channel("rss_2.0").apply {
            title = "토이 프로젝트 - 게시판"
            link = "https://github.com/antop-dev/toy-board"
            description = "점진적으로 발전해나가는 게시판 프로젝트"
            pubDate = LocalDateTime.now().toJavaDate()
            items = posts.items.map { buildItem(request, it) }
        }
    }

    private fun buildItem(
        request: HttpServletRequest,
        post: PostQuery,
    ) = Item().apply {
        title = post.subject
        link = buildViewUri(request, post.id)
        pubDate = post.created.toJavaDate()
        author = post.author.nickname
        categories = post.tags?.map { tag -> Category().apply { value = tag } }
    }
}
