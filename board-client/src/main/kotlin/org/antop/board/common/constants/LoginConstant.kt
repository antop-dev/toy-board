package org.antop.board.common.constants

object LoginConstant {
    object Url {
        const val LOGIN_FORM = "/login.html"
        const val LOGIN_PROCESSING = "/login"
    }

    object Parameter {
        const val USERNAME = "username"
        const val PASSWORD = "password"
        const val REMEMBER_ME = "remember-me"
    }

    const val SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST"
}
