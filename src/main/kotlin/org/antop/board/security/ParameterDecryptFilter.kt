package org.antop.board.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter

class ParameterDecryptFilter(
    private val rsaDecrypter: RsaDecrypter,
    private val aesDecrypter: AesDecrypter,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val encrypted =
            try {
                request.getHeader("X-Encrypted")?.toBoolean() ?: false
            } catch (_: Exception) {
                false
            }
        // 암호화한 요청인 경우에만 처리한다.
        val wrapper = if (encrypted) ParameterDecryptRequestWrapper(request, rsaDecrypter, aesDecrypter) else request
        filterChain.doFilter(wrapper, response)
    }
}
