package org.antop.board.security

import java.nio.ByteBuffer
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class AesDecrypter {
    companion object {
        const val ALGORITHM = "AES"
        const val GCM_TAG_LENGTH = 128
        const val TRANSFORMATION = "AES/GCM/NoPadding"
    }

    /**
     * AES 복호화
     *
     * @param encrypted 암호화된 값
     * @param key AES KEY
     * @param iv IV(초기화 벡터)
     * @param tag 태그
     * @param aad 추가 인증 데이터 (Additional Authenticated Data)
     */
    fun decrypt(
        encrypted: ByteArray,
        key: ByteArray,
        iv: ByteArray,
        tag: ByteArray,
        aad: ByteArray?,
    ): ByteArray {
        val secretKey = SecretKeySpec(key, ALGORITHM)
        val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        val cipher =
            Cipher.getInstance(TRANSFORMATION).apply {
                init(Cipher.DECRYPT_MODE, secretKey, gcmSpec)
                aad?.let { updateAAD(it) }
            }
        // 복호화
        val cipherTextWithTag = cipherTextWithTag(encrypted, tag)
        return cipher.doFinal(cipherTextWithTag)
    }

    // 암호화된 값과 TAG를 붙인다
    private fun cipherTextWithTag(
        cipherText: ByteArray,
        authTag: ByteArray,
    ) = ByteBuffer
        .allocate(cipherText.size + authTag.size)
        .put(cipherText)
        .put(authTag)
        .array()
}
