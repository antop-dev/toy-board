package org.antop.board.member.controller

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest
import jakarta.servlet.http.HttpServletRequest
import org.antop.board.captcha.ValidCaptcha
import org.antop.board.common.constants.MemberConsts
import org.antop.board.common.extensions.contextPath
import org.antop.board.member.service.MemberService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class MemberController(
    private val memberService: MemberService,
) {
    @GetMapping(MemberConsts.Url.REGISTER_FORM)
    fun registerForm(model: Model): String {
        model.addAttribute("registerProcessingUrl", MemberConsts.Url.REGISTER_PROCESSING)
        return "members/register"
    }

    @HxRequest
    @ValidCaptcha
    @PostMapping(MemberConsts.Url.REGISTER_PROCESSING)
    fun registerProcess(
        @RequestParam("email") email: String,
        @RequestParam("password1") password1: String,
        @RequestParam("password2") password2: String,
        @RequestParam("nickname") nickname: String,
        request: HttpServletRequest,
    ): HtmxResponse {
        memberService.register(email, password1, password2, nickname)
        return HtmxResponse
            .builder()
            .redirect(request.contextPath())
            .build()
    }
}
