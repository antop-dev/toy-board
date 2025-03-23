package org.antop.board.common.extensions

import jakarta.servlet.http.HttpServletRequest

fun HttpServletRequest.contextPath() = contextPath.takeIf { it.isNotBlank() } ?: "/"
