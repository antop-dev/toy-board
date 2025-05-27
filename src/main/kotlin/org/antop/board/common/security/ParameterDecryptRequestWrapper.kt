package org.antop.board.common.security

import extensions.decodeHex
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper

class ParameterDecryptRequestWrapper(
    request: HttpServletRequest,
    private val rsaDecrypter: RsaDecrypter,
    private val aesDecrypter: AesDecrypter,
) : HttpServletRequestWrapper(request) {
    val parameters: Map<String, Array<String>>

    init {
        parameters =
            request.parameterMap
                .map { (name, values) ->
                    name to decrypt(values)
                }.toMap()
    }

    override fun getParameterValues(name: String) = parameters[name]

    override fun getParameter(name: String) = parameters[name]?.let { it[0] }

    override fun getParameterMap() = parameters.toMap()

    private fun decrypt(values: Array<String>) = values.map { decrypt(it) }.toTypedArray()

    private fun decrypt(data: String): String {
        // 512, 32, x, 32, 64
        val keyWithRsaHex = data.slice(0 until 512)
        val ivHex = data.slice(512 until 512 + 32)
        val encryptedHex = data.slice(512 + 32 until data.length - 32 - 64)
        val tagHex = data.slice(data.length - 32 - 64 until data.length - 64)
        val aadHex = data.substring(data.length - 64 until data.length)
        val aesKey = rsaDecrypter.decrypt(keyWithRsaHex.decodeHex())
        val decrypted =
            aesDecrypter.decrypt(
                encrypted = encryptedHex.decodeHex(),
                key = aesKey,
                iv = ivHex.decodeHex(),
                tag = tagHex.decodeHex(),
                aad = aadHex.decodeHex(),
            )
        return String(decrypted)
    }
}
