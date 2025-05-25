package org.antop.board.member.controller

import extensions.createUri
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.antop.board.common.captcha.ValidCaptcha
import org.antop.board.common.security.RsaProvider
import org.antop.board.common.valid.NotExpireToken
import org.antop.board.member.service.PasswordResetService
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Validated
@Controller
@RequestMapping("/members")
class PasswordResetController(
    private val rsaProvider: RsaProvider,
    private val passwordResetService: PasswordResetService,
) {
    @GetMapping("/forgot-password.html")
    fun forgotPassword(model: Model): String {
        model.addAttribute("rsaPublicKey", rsaProvider.publicKeyString)
        return "members/forgot-password"
    }

    /** 비밀번호 초기화 요청 */
    @HxRequest
    @ValidCaptcha
    @PostMapping("/forgot-password")
    @ResponseBody
    fun requestResetPassword(
        @RequestParam("email") email: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        passwordResetService.forgetPassword(email)
        response.addHeader(HttpHeaders.LOCATION, request.createUri("/login.html"))
    }

    /** 비밀번호 초기화 화면 */
    @GetMapping("/password-reset.html")
    fun resetPasswordForm(
        @Valid @NotExpireToken @RequestParam("token") token: String,
        model: Model,
    ): String {
        model.addAttribute("token", token)
        model.addAttribute("rsaPublicKey", rsaProvider.publicKeyString)
        return "members/password-reset"
    }

    /** 비밀번호 초기화 처리 */
    @HxRequest
    @ValidCaptcha
    @PostMapping("/password-reset")
    @ResponseBody
    fun performResetPassword(
        @NotExpireToken @RequestParam("token") token: String,
        @NotBlank @RequestParam("password") password: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        passwordResetService.resetPassword(token, password)
        response.addHeader(HttpHeaders.LOCATION, request.createUri("/login.html"))
    }
}
