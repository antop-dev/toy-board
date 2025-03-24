package org.antop.board.captcha

import jakarta.validation.ValidationException

class CaptchaInvalidException : ValidationException("캡차 값이 유효하지 않습니다.")
