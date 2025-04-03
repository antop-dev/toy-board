package org.antop.board.member.controller

import jakarta.servlet.http.HttpServletRequest
import org.antop.board.common.constants.LoginConsts
import org.springframework.http.HttpHeaders
import org.springframework.security.web.savedrequest.SimpleSavedRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginController {
    @GetMapping(LoginConsts.Url.LOGIN_FORM)
    fun form(
        model: Model,
        request: HttpServletRequest,
    ): String {
        request.getHeader(HttpHeaders.REFERER)?.let {
            val savedRequest = SimpleSavedRequest(it)
            request.session.setAttribute(LoginConsts.SAVED_REQUEST, savedRequest)
        }

        model.addAttribute("loginProcessingUrl", LoginConsts.Url.LOGIN_PROCESSING)
        model.addAttribute("usernameParameter", LoginConsts.Parameter.USERNAME)
        model.addAttribute("passwordParameter", LoginConsts.Parameter.PASSWORD)
        model.addAttribute("rememberMeParameter", LoginConsts.Parameter.REMEMBER_ME)
        return "login"
    }
}
