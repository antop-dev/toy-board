package org.antop.board.member.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.antop.board.common.mail.MailService
import org.antop.board.common.token.TokenProvider
import org.antop.board.member.exception.EmailNotFoundException
import org.antop.board.member.exception.MemberNotFoundException
import org.antop.board.member.properties.PasswordResetProperties
import org.antop.board.member.properties.SiteProperties
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.util.UriComponentsBuilder

@Service
class PasswordResetService(
    private val siteProperties: SiteProperties,
    private val passwordResetProperties: PasswordResetProperties,
    private val memberService: MemberService,
    private val mailService: MailService,
    private val tokenProvider: TokenProvider,
) {
    private val log = KotlinLogging.logger { }

    fun forgetPassword(email: String) {
        val member =
            try {
                memberService.getMember(email)
            } catch (_: MemberNotFoundException) {
                throw EmailNotFoundException()
            }
        val token = tokenProvider.create(member.email)
        val link =
            UriComponentsBuilder
                .fromUriString(siteProperties.url)
                .pathSegment("members", "password-reset.html")
                .queryParam("token", token)
                .build()
                .toUriString()
        log.debug { "password reset. email = ${member.email}, link = $link" }
        mailService.passwordReset(email, link, passwordResetProperties.tokenTimeout)
    }

    @Transactional
    fun resetPassword(
        token: String,
        password: String,
    ) {
        val email = tokenProvider.parse(token)
        memberService.changePassword(email, password)
    }
}
