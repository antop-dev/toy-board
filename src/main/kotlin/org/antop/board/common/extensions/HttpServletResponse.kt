package org.antop.board.common.extensions

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponseHeader
import jakarta.servlet.http.HttpServletResponse

fun HttpServletResponse.setHeader(
    htmxResponseHeader: HtmxResponseHeader,
    contextPath: String,
) {
    setHeader(htmxResponseHeader.value, contextPath)
}
