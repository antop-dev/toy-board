package extensions

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.util.UriComponentsBuilder

fun HttpServletRequest.contextPath() = contextPath.takeIf { it.isNotBlank() } ?: "/"

fun HttpServletRequest.createUri(path: String) =
    UriComponentsBuilder
        .fromUriString(this.requestURI)
        .replacePath(path)
        .build()
        .toUriString()
