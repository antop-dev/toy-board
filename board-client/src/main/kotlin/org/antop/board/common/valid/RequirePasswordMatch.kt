package org.antop.board.common.valid

/**
 * 이 어노테이션은 비밀번호가 일치하는지 확인하기 위해 사용됩니다.
 * 주로 회원 가입이나 비밀번호 변경과 같은 작업에서 사용됩니다.
 *
 * @property parameter 검증할 비밀번호 파라미터의 이름. 기본값은 "password"입니다.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequirePasswordMatch(
    val parameter: String = "password",
)
