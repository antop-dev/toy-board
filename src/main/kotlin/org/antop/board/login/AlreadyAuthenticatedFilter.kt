package org.antop.board.login

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.antop.board.common.extensions.contextPath
import org.antop.board.common.extensions.isAnonymous
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

/**
 * 로그인 폼으로 요청이 왔는데 이미 로그인 되어 있으면 리다이렉트
 */
class AlreadyAuthenticatedFilter(
    private vararg val patterns: String,
) : OncePerRequestFilter() {
    private val matcher: AntPathMatcher = AntPathMatcher()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val match = patterns.any { matcher.match(it, request.requestURI) }
        if (!match) {
            filterChain.doFilter(request, response)
            return
        }

        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        if (authentication != null && authentication.isAuthenticated && !authentication.isAnonymous) {
            response.sendRedirect(request.contextPath())
            return
        }

        filterChain.doFilter(request, response)
    }
}
