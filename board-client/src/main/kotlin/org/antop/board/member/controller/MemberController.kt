package org.antop.board.member.controller

import extensions.contextPath
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest
import jakarta.servlet.http.HttpServletRequest
import org.antop.board.common.captcha.ValidCaptcha
import org.antop.board.common.constants.MemberConstant
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
    @GetMapping(MemberConstant.Url.REGISTER_FORM)
    fun registerForm(model: Model): String {
        model.addAttribute("registerProcessingUrl", MemberConstant.Url.REGISTER_PROCESSING)
        return "members/register"
    }

    @HxRequest
    @ValidCaptcha
    @PostMapping(MemberConstant.Url.REGISTER_PROCESSING)
    fun registerProcess(
        @RequestParam("email") email: String,
        @RequestParam("password") password: String,
        @RequestParam("nickname") nickname: String,
        @RequestParam("avatar") avatar: String?,
        request: HttpServletRequest,
    ): HtmxResponse {
        memberService.register(email, password, nickname, avatar)
        return HtmxResponse
            .builder()
            .redirect(request.contextPath())
            .build()
    }
}
