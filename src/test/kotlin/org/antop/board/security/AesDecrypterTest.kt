package org.antop.board.security

import org.antop.board.common.extensions.decodeHex
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test

class AesDecrypterTest {
    val decrypter = AesDecrypter()

    @Test
    fun decrypt() {
        val encryptedHex = "67372dd642ddcf93c96b2a373942e8"
        val keyHex = "b25fdc75b450551dc393c553328af022" // 32
        val ivHex = "cd24ff23392ff95923cbe281a95b2137" // 32
        val tagHex = "06730e6b615d21e7597e3bbda311d854" // 32
        val addHex = "ac22b2ef4556716fd714e9adc6caa1ceab212ddc99bd565587dd182418c8c54e" // 64

        val decrypted =
            decrypter.decrypt(
                encryptedHex.decodeHex(),
                keyHex.decodeHex(),
                ivHex.decodeHex(),
                tagHex.decodeHex(),
                addHex.decodeHex(),
            )

        assertThat(String(decrypted), `is`("antop@kakao.com"))
    }
}
