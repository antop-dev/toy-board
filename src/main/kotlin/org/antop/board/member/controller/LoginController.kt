package org.antop.board.member.controller

import jakarta.servlet.http.HttpServletRequest
import org.antop.board.common.constants.LoginConstant
import org.antop.board.common.constants.MemberConstant
import org.antop.board.security.RsaProvider
import org.springframework.http.HttpHeaders
import org.springframework.security.web.savedrequest.SimpleSavedRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.util.Base64

@Controller
class LoginController(
    private val rsaProvider: RsaProvider,
) {
    @GetMapping(LoginConstant.Url.LOGIN_FORM)
    fun form(
        model: Model,
        request: HttpServletRequest,
    ): String {
        request.getHeader(HttpHeaders.REFERER)?.let {
            val savedRequest = SimpleSavedRequest(it)
            request.session.setAttribute(LoginConstant.SAVED_REQUEST, savedRequest)
        }

        model.addAttribute("loginProcessingUrl", LoginConstant.Url.LOGIN_PROCESSING)
        model.addAttribute("memberRegisterUrl", MemberConstant.Url.REGISTER_FORM)
        model.addAttribute("usernameParameter", LoginConstant.Parameter.USERNAME)
        model.addAttribute("passwordParameter", LoginConstant.Parameter.PASSWORD)
        model.addAttribute("rememberMeParameter", LoginConstant.Parameter.REMEMBER_ME)
        model.addAttribute("rsaPublicKey", rsaPublicKey())

        return "login"
    }

    private fun rsaPublicKey() =
        buildString {
            val base64 = Base64.getEncoder().encodeToString(rsaProvider.publicKey.encoded)
            append("-----BEGIN PUBLIC KEY-----")
            append(base64)
            append("-----END PUBLIC KEY-----")
        }
}
