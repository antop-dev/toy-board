package org.antop.board.security

import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

@Component
class RsaResourceProvider(
    rsaResourceProperties: RsaResourceProperties,
) : RsaProvider {
    private val _publicKey: PublicKey

    override val publicKey: PublicKey
        get() = this._publicKey

    init {
        _publicKey = loadPublicKey(rsaResourceProperties.publicKey)
    }

    private fun loadPublicKey(resource: Resource): PublicKey {
        val pem = parsePemFile(resource)
        val decoded = Base64.getDecoder().decode(pem)
        val keySpec = X509EncodedKeySpec(decoded)
        return KeyFactory.getInstance("RSA").generatePublic(keySpec)
    }

    private fun parsePemFile(resource: Resource): String =
        Files
            .readString(resource.file.toPath())
            .replace("-----BEGIN (.*)-----|-----END (.*)-----|\\s+".toRegex(), "")
}
