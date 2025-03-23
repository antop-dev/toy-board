package org.springframework.security.crypto.password

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.factory.PasswordEncoderFactories

class PasswordEncoderTest {
    private val encoder: PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Test
    fun crypt() {
        val plain = "antop"
        val encoded = encoder.encode(plain)
        println("encoded = $encoded")
        assertThat(encoder.matches(plain, encoded), `is`(true))
    }
}
