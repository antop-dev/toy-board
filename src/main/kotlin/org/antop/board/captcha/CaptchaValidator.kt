package org.antop.board.captcha

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Aspect
@Component
class CaptchaValidator(
    private val session: HttpSession,
    private val request: HttpServletRequest,
) {
    @Pointcut("@annotation(validCaptcha)")
    fun validCaptchaMethod(validCaptcha: ValidCaptcha) {
        // pointcut
    }

    @Before("validCaptchaMethod(validCaptcha)")
    fun validateCaptcha(
        joinPoint: JoinPoint,
        validCaptcha: ValidCaptcha,
    ) {
        // 캡챠 이미지를 만들 때 세션에 저장했던 캡챠 코드 값
        val captchaCode = session.getAttribute(validCaptcha.session) as? String
        // 폼에서 입력한 캡챠 코드 값
        val input = request.getParameter(validCaptcha.param)
        // 비교
        if (captchaCode != input) {
            throw CaptchaInvalidException()
        }
    }
}
