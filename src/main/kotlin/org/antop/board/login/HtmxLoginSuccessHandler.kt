package org.antop.board.login

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponseHeader
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.antop.board.common.extensions.contextPath
import org.antop.board.common.extensions.setHeader
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.security.web.savedrequest.SavedRequest

class HtmxLoginSuccessHandler : SimpleUrlAuthenticationSuccessHandler() {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        response.status = HttpServletResponse.SC_OK
        response.setHeader(HtmxResponseHeader.HX_REDIRECT, redirectUrl(request))
    }

    /**
     * 원래 접근하려 했던 URL 가져오기
     * list.html -> save.html -- 로그인x --> login.html --> 로그인됨 -> save.html
     */
    private fun redirectUrl(request: HttpServletRequest): String {
        // 원래 사용자가 접근하려던 URL 가져오기
        val savedRequest = request.session.getAttribute("SPRING_SECURITY_SAVED_REQUEST") as SavedRequest?
        return savedRequest?.redirectUrl ?: request.contextPath()
    }
}
