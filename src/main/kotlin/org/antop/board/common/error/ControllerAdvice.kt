package org.antop.board.common.error

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.ConstraintViolationException
import jakarta.validation.ValidationException
import org.antop.board.common.exceptions.NotFoundException
import org.antop.board.common.exceptions.SecretPostException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice(annotations = [Controller::class])
class ControllerAdvice {
    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun constraintViolationException(
        e: ConstraintViolationException,
        response: HttpServletResponse,
    ) {
        val message = e.constraintViolations.firstOrNull()?.message ?: e.message ?: "잘못된 요청입니다."
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, message)
    }

    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun validationException(
        e: ValidationException,
        response: HttpServletResponse,
    ) {
        val message = e.cause?.message ?: e.message ?: "잘못된 요청입니다."
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, message)
    }

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFoundException() = "/error/404"

    @ExceptionHandler(SecretPostException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun secretPostException() = "/error/403"
}
