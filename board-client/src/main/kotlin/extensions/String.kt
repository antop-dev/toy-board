package extensions

import org.apache.commons.codec.binary.Hex

/** HEX 문자열을 바이트로 변경 */
fun String.decodeHex(): ByteArray = Hex.decodeHex(this)
