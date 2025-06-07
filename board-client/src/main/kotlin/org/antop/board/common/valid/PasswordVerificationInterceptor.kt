package org.antop.board.common.valid

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.antop.board.common.exceptions.PasswordNotMatchException
import org.antop.board.login.UserPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

/**
 * 이 인터셉터는 `@RequirePasswordMatch` 어노테이션이 적용된 보호된 경로에 대한
 * 비밀번호 검증을 처리합니다. 요청에서 제공된 사용자의 비밀번호가 인증된 사용자의
 * 저장된 비밀번호와 일치하는지 확인합니다.
 *
 * `@RequirePasswordMatch` 어노테이션은 비밀번호 검증이 필요한 메소드에 적용되어야 합니다.
 * 요청에서 비밀번호를 포함하는 파라미터는 `@RequirePasswordMatch` 어노테이션의
 * `parameter` 속성을 통해 구성할 수 있습니다.
 *
 * 인터셉터는 보안 컨텍스트에서 인증된 사용자의 세부 정보를 검색하고
 * `PasswordEncoder`를 사용하여 비밀번호를 검증합니다. 인증 정보가 없거나
 * 비밀번호가 일치하지 않는 경우, 인터셉터는 `PasswordNotMatchException`을 발생시킵니다.
 *
 * 구현:
 * - HandlerInterceptor: HTTP 요청의 전처리를 허용합니다.
 *
 * 예외:
 * - PasswordNotMatchException: 인증 정보가 없거나 비밀번호가 일치하지 않는 경우 발생합니다.
 */
class PasswordVerificationInterceptor(
    private val passwordEncoder: PasswordEncoder,
) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }

        val annotation = handler.getMethodAnnotation(RequirePasswordMatch::class.java)
        if (annotation == null) {
            return true
        }

        val principal =
            SecurityContextHolder.getContext().authentication.principal as? UserPrincipal
                ?: throw PasswordNotMatchException("인증 정보가 없습니다.")

        val password =
            request.getParameter(annotation.parameter)
                ?: throw PasswordNotMatchException("비밀번호가 필요합니다.")

        if (!passwordEncoder.matches(password, principal.password)) {
            throw PasswordNotMatchException("비밀번호가 일치하지 않습니다.")
        }

        return true
    }
}
