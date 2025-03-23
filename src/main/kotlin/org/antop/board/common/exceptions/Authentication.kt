package org.antop.board.common.exceptions

import org.springframework.security.core.Authentication

val Authentication.isAnonymous: Boolean
    get() {
        return principal == "anonymousUser"
    }
