package org.antop.board.common.valid

import io.jsonwebtoken.ExpiredJwtException
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.antop.board.common.token.JwtTokenProvider

/** 토큰이 만료되지 않았는지 체크한다. */
class NotExpireJwtValidator(
    private val tokenManager: JwtTokenProvider,
) : ConstraintValidator<NotExpireToken, String> {
    override fun isValid(
        token: String?,
        context: ConstraintValidatorContext,
    ): Boolean {
        if (token == null) {
            return false
        }
        return try {
            tokenManager.parse(token)
            true
        } catch (_: ExpiredJwtException) {
            false
        } catch (e: Exception) {
            throw e
        }
    }
}
