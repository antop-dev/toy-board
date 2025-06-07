package org.antop.board.member.controller

import extensions.contextPath
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest
import jakarta.servlet.http.HttpServletRequest
import org.antop.board.common.captcha.ValidCaptcha
import org.antop.board.common.constants.MemberConstant
import org.antop.board.common.valid.RequirePasswordMatch
import org.antop.board.login.UserPrincipal
import org.antop.board.member.dto.MemberDto
import org.antop.board.member.service.MemberService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
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

    /**
     * 회원 정보 수정 폼을 보여줍니다.
     *
     * @param model 모델 객체
     * @param principal 인증된 사용자 정보
     * @return 회원 정보 수정 폼의 뷰 이름
     */
    @GetMapping("/members/modify.html")
    fun modifyForm(
        model: Model,
        @AuthenticationPrincipal principal: UserPrincipal,
    ): String {
        val member = memberService.getMember(principal.username)
        model.addAttribute("member", member)

        return "members/modify"
    }

    /**
     * 회원 정보 수정을 처리합니다.
     *
     * @param principal 인증된 사용자 정보
     * @param nickname 새 닉네임
     * @param avatar 새 아바타 URL (선택 사항)
     * @param request HTTP 요청 객체
     * @return HTMX 응답 객체
     */
    @HxRequest
    @PostMapping("/members/modify")
    @RequirePasswordMatch
    fun modifyProcess(
        @AuthenticationPrincipal principal: UserPrincipal,
        @RequestParam("nickname") nickname: String,
        @RequestParam("avatar", required = false) avatar: String?,
        request: HttpServletRequest,
    ): HtmxResponse {
        val member = memberService.modify(principal.username, nickname, avatar)
        member?.let { changePrincipal(it, request) }
        return HtmxResponse
            .builder()
            .redirect(request.contextPath())
            .build()
    }

    /**
     * 업데이트된 회원 정보를 반영하여 보안 컨텍스트와 세션의 `UserPrincipal` 인스턴스를 갱신합니다.
     *
     * @param member `MemberDto` 객체에 담긴 업데이트된 회원 정보
     * @param request 새로운 보안 컨텍스트로 세션을 업데이트하는데 사용되는 현재 HTTP 요청 객체
     */
    private fun changePrincipal(
        member: MemberDto,
        request: HttpServletRequest,
    ) {
        // 변경된 회원 정보를 반영한 새로운 UserPrincipal 생성
        val newPrincipal =
            UserPrincipal(
                id = member.id,
                email = member.email,
                password = member.password,
                nickname = member.nickname,
                avatar = member.avatar,
            )
        // 변경된 인증 정보를 세션에 저장하여 Spring Security가 인식하도록 함
        val context = SecurityContextHolder.getContext()
        context.authentication =
            UsernamePasswordAuthenticationToken(newPrincipal, null, listOf(UserPrincipal.DEFAULT_ROLE))
        request.session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context)
    }
}
