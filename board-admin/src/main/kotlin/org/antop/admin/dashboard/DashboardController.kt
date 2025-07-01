package org.antop.admin.dashboard

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.view.RedirectView

@Controller
class DashboardController {
    @GetMapping("/")
    fun index() = RedirectView("/dashboard")

    @GetMapping("/dashboard")
    fun dashboard() = "dashboard/index"
}
