package org.antop.board.login

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponseHeader
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.antop.board.common.constants.LoginConsts
import org.antop.board.common.extensions.setHeader
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler

class HtmxLogoutSuccessHandler : LogoutSuccessHandler {
    override fun onLogoutSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        response.status = HttpServletResponse.SC_OK
        response.setHeader(HtmxResponseHeader.HX_REDIRECT, LoginConsts.Url.LOGIN_FORM)
    }
}
