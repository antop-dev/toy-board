package org.antop.board.feed

import com.rometools.rome.feed.atom.Content
import com.rometools.rome.feed.atom.Entry
import com.rometools.rome.feed.atom.Feed
import com.rometools.rome.feed.atom.Person
import extensions.now
import extensions.toJavaDate
import jakarta.servlet.http.HttpServletRequest
import kotlinx.datetime.LocalDateTime
import org.antop.board.post.dto.AuthorQuery
import org.antop.board.post.dto.PostQuery
import org.antop.board.post.service.PostService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AtomController(
    private val postService: PostService,
) : FeedHelper() {
    @GetMapping("/feed/atom", produces = [MediaType.APPLICATION_ATOM_XML_VALUE])
    fun atom(request: HttpServletRequest): Feed {
        val posts = postService.getPosts()
        return Feed("atom_1.0").apply {
            title = "토이 프로젝트 - 게시판"
            id = "https://github.com/antop-dev/toy-board"
            updated = LocalDateTime.now().toJavaDate()
            info = Content().apply { value = "점진적으로 잘전해나가는 게시판 프로젝트" }
            entries = posts.items.map { buildEntry(request, it) }
        }
    }

    private fun buildEntry(
        request: HttpServletRequest,
        post: PostQuery,
    ) = Entry().apply {
        title = post.subject
        id = buildViewUri(request, post.id)
        updated = post.created.toJavaDate()
        authors = listOf(buildPerson(post.author))
    }

    private fun buildPerson(author: AuthorQuery) =
        Person().apply {
            name = author.nickname
            email = author.email
        }
}
