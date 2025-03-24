package org.antop.board.common.error

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.ValidationException
import org.antop.board.common.exceptions.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice(annotations = [Controller::class])
class ControllerAdvice {
    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun validationException(
        e: ValidationException,
        response: HttpServletResponse,
    ) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.message)
    }

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFoundException() = "/error/404"
}
