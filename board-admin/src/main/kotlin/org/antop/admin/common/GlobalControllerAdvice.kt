package org.antop.admin.common

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute

@ControllerAdvice
class GlobalControllerAdvice {
    @ModelAttribute("currentUrl")
    fun currentUrl(request: HttpServletRequest): String = request.requestURI

    @ModelAttribute("contextPath")
    fun contextPath(request: HttpServletRequest): String = request.contextPath
}
