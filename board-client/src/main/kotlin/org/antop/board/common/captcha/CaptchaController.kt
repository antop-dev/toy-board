package org.antop.board.common.captcha

import com.mewebstudio.captcha.Captcha
import com.mewebstudio.captcha.Config
import com.mewebstudio.captcha.GeneratedCaptcha
import com.mewebstudio.captcha.util.RandomStringGenerator
import jakarta.servlet.http.HttpSession
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.ContentDisposition
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.io.ByteArrayOutputStream
import java.security.SecureRandom
import javax.imageio.ImageIO

@Controller
class CaptchaController {
    companion object {
        private const val CAPTCHA_LENGTH = 6
        private const val CAPTCHA_NOISE = 5
        private const val CAPTCHA_SYMBOLS = "0123456789"
    }

    @GetMapping("/captcha.png")
    fun captcha(
        session: HttpSession,
        @RequestParam("w", required = false) width: Int = 200,
        @RequestParam("h", required = false) height: Int = 50,
    ): ResponseEntity<ByteArrayResource> {
        val captcha = createCaptcha(width, height)
        session.setAttribute("captcha", captcha.code)
        return ok(captcha)
    }

    fun createCaptcha(
        width: Int,
        height: Int,
    ): GeneratedCaptcha {
        val config =
            Config().apply {
                this.width = width
                this.height = height
                noise = CAPTCHA_NOISE
                isDark = false
            }

        val captcha =
            Captcha().apply {
                this.config = config

                val randomStringGenerator = RandomStringGenerator(CAPTCHA_LENGTH, SecureRandom(), CAPTCHA_SYMBOLS)
                setRandomStringGenerator(randomStringGenerator)
            }

        return captcha.generate()
    }

    fun ok(captcha: GeneratedCaptcha): ResponseEntity<ByteArrayResource> {
        ByteArrayOutputStream().use { output ->
            ImageIO.write(captcha.image, "png", output)
            return ResponseEntity
                .ok()
                .headers {
                    it.contentDisposition = ContentDisposition.inline().filename("captcha.png").build()
                    it.contentType = MediaType.IMAGE_PNG
                    it.contentLength = output.size().toLong()
                }.body(ByteArrayResource(output.toByteArray()))
        }
    }
}
