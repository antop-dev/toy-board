package org.antop.board.security

import java.security.spec.MGF1ParameterSpec
import javax.crypto.Cipher
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource

class RsaDecrypter(
    private val rsaProvider: RsaProvider,
) {
    fun decrypt(encrypted: ByteArray): ByteArray {
        val cipher =
            Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding").apply {
                val oaepSpec =
                    OAEPParameterSpec(
                        "SHA-256",
                        "MGF1",
                        MGF1ParameterSpec.SHA256,
                        PSource.PSpecified.DEFAULT,
                    )
                init(Cipher.DECRYPT_MODE, rsaProvider.privateKey, oaepSpec)
            }
        val decrypted = cipher.doFinal(encrypted)
        return decrypted
    }
}
