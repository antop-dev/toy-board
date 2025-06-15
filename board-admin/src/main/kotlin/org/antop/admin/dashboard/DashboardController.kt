package org.antop.admin.dashboard

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class DashboardController {
    @GetMapping("/")
    fun index(
        model: Model,
        authentication: Authentication,
    ): String {
        model.addAttribute("name", authentication.name)
        return "dashboard"
    }
}
