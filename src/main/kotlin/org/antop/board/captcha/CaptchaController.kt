package org.antop.board.captcha

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
        val config = Config(width, height, 6, 5, false)

        val symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZ23456789"
        val randomStringGenerator = RandomStringGenerator(6, SecureRandom(), symbols)

        val captcha = Captcha()
        captcha.config = config
        captcha.setRandomStringGenerator(randomStringGenerator)

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
