package org.antop.board.feed

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.util.UriComponentsBuilder

open class FeedHelper {
    protected fun buildViewUri(
        request: HttpServletRequest,
        postId: Long,
    ) = UriComponentsBuilder
        .fromUriString(request.requestURL.toString())
        .replacePath("/posts/view.html")
        .queryParam("id", postId)
        .build()
        .toString()
}
