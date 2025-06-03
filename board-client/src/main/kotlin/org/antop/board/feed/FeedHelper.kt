package org.antop.board.feed

import jakarta.servlet.http.HttpServletRequest
import org.antop.board.common.constants.PostConstant
import org.springframework.web.util.UriComponentsBuilder

open class FeedHelper {
    protected fun buildViewUri(
        request: HttpServletRequest,
        postId: Long,
    ) = UriComponentsBuilder
        .fromUriString(request.requestURL.toString())
        .replacePath(PostConstant.Url.VIEW)
        .queryParam("id", postId)
        .build()
        .toString()
}
