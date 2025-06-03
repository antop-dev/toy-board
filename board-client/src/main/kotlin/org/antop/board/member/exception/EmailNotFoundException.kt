package org.antop.board.member.exception

import jakarta.validation.ValidationException

class EmailNotFoundException : ValidationException("이메일이 존재하지 않습니다.")
