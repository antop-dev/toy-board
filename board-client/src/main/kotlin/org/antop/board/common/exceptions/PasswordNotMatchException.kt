package org.antop.board.common.exceptions

import jakarta.validation.ValidationException

/**
 * 비밀번호 검증 실패 시 발생하는 예외.
 *
 * @param message 예외의 원인을 설명하는 상세 메시지.
 */
class PasswordNotMatchException(
    message: String,
) : ValidationException(message)
