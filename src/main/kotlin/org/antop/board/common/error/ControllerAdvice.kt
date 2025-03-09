package org.antop.board.common.error

import org.antop.board.captcha.CaptchaInvalidException
import org.antop.board.common.exceptions.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice(annotations = [Controller::class])
class ControllerAdvice {
    @ExceptionHandler(CaptchaInvalidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun captchaInvalidException() {
        // TODO: AJAX(HTMX) 공통 응답 패턴 정의
    }

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFoundException() = "/error/404"
}
