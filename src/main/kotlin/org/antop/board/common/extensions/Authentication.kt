package org.antop.board.common.extensions

import org.springframework.security.core.Authentication

val Authentication.isAnonymous: Boolean
    get() {
        return principal == "anonymousUser"
    }
