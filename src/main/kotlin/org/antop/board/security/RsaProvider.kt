package org.antop.board.security

import java.security.PrivateKey
import java.security.PublicKey

interface RsaProvider {
    /** RSA 개인키 */
    val privateKey: PrivateKey

    /** RSA 공개키 */
    val publicKey: PublicKey
}
