package org.antop.board.security

import java.security.PublicKey

interface RsaProvider {
    /** RSA 공개키 */
    val publicKey: PublicKey
}
