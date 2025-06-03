package org.antop.board.common.security

import java.security.PublicKey

interface RsaProvider {
    /** RSA 공개키 */
    val publicKey: PublicKey

    /** RSA 공개키 문자열 (pem) */
    val publicKeyString: String
}
