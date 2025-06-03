package extensions

import org.springframework.security.core.Authentication

val Authentication.isAnonymous: Boolean
    get() {
        return principal == "anonymousUser"
    }

/**
 * 인증 되었고 익명 유저도 아님
 */
val Authentication.isRealAuthenticated: Boolean
    get() = isAuthenticated && !isAnonymous
