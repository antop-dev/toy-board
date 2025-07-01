package org.antop.admin.member

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/member")
class MemberController {
    @GetMapping
    fun index() = "member/index"
}
