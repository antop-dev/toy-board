package org.antop.board.common.exceptions

import jakarta.validation.ValidationException

class EmailAlreadyExistsException(
    val email: String,
) : ValidationException("이미 존재하는 이메일입니다.")
