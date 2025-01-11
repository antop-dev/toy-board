package org.antop.board.common

object Base62 {
    private const val ALPHABET = "BoiacQECWd9qXlw6JvFVkTusADpm8KHjNIn7g3xYhS405Ze2PzRytG1MfbUrLO"
    private const val BASE = ALPHABET.length
    private val CHAR_TO_INDEX = ALPHABET.withIndex().associate { it.value to it.index }

    /**
     * Long 값을 Base62 문자열로 변환한다.
     */
    fun encode(number: Long): String {
        require(number >= 0) { "Number must be non-negative." }
        var value = number
        val encoded = StringBuilder()
        while (value > 0) {
            val index = (value % BASE).toInt()
            encoded.append(ALPHABET[index])
            value /= BASE
        }
        return if (encoded.isEmpty()) "0" else encoded.reverse().toString()
    }

    /**
     * Base62 문자열을 Long 값으로 변환한다.
     */
    fun decode(base62: String): Long {
        require(base62.isNotEmpty()) { "Input string must not be empty." }

        var result = 0L
        for (char in base62) {
            val index =
                CHAR_TO_INDEX[char]
                    ?: throw IllegalArgumentException("Invalid character '$char' in Base62 string.")
            result = (result * BASE) + index
        }
        return result
    }
}
