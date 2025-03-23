package org.antop.board.member.controller

import org.antop.board.common.constants.LoginConsts
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginController {
    @GetMapping(LoginConsts.Url.LOGIN_FORM)
    fun form(model: Model): String {
        model.addAttribute("loginProcessingUrl", LoginConsts.Url.LOGIN_PROCESSING)
        model.addAttribute("usernameParameter", LoginConsts.Parameter.USERNAME)
        model.addAttribute("passwordParameter", LoginConsts.Parameter.PASSWORD)
        model.addAttribute("rememberMeParameter", LoginConsts.Parameter.REMEMBER_ME)
        return "login"
    }
}
