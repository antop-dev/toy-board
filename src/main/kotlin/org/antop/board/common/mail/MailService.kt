package org.antop.board.common.mail

import extensions.koreanUnit
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class MailService(
    private val mailSender: MailSender,
) {
    fun passwordReset(
        email: String,
        link: String,
        expiration: Duration,
    ) {
        val message =
            SimpleMailMessage().apply {
                from = "noreply@localhost"
                setTo(email)
                subject = "비밀번호 재설정 안내"
                text = "아래 링크를 클릭하여 비밀번호를 재설정하세요. 유효기간은 ${expiration.koreanUnit()}입니다.\n\n$link"
            }
        mailSender.send(message)
    }
}
