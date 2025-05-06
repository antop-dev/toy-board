package org.antop.board.security

import org.springframework.core.io.Resource
import java.nio.file.Files
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.MGF1ParameterSpec
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource

class RsaDecrypter(
    rsaResourceProperties: RsaResourceProperties,
) {
    private val privateKey: PrivateKey = loadPrivateKey(rsaResourceProperties.privateKey)

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
                init(Cipher.DECRYPT_MODE, privateKey, oaepSpec)
            }
        val decrypted = cipher.doFinal(encrypted)
        return decrypted
    }

    private fun loadPrivateKey(resource: Resource): PrivateKey {
        val pem = parsePemFile(resource)
        val decoded = Base64.getDecoder().decode(pem)
        val keySpec = PKCS8EncodedKeySpec(decoded)
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec)
    }

    private fun parsePemFile(resource: Resource): String =
        Files
            .readString(resource.file.toPath())
            .replace("-----BEGIN (.*)-----|-----END (.*)-----|\\s+".toRegex(), "")
}
