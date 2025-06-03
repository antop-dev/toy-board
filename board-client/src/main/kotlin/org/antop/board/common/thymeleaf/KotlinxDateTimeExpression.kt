package org.antop.board.common.thymeleaf

import extensions.pretty
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

class KotlinxDateTimeExpression {
    fun pretty(vararg dateTimes: LocalDateTime?): String? =
        dateTimes
            .filterNotNull()
            .map { it.pretty() }
            .firstOrNull()

    @OptIn(FormatStringsInDatetimeFormats::class)
    fun format(
        dateTime: LocalDateTime?,
        pattern: String,
    ): String? = dateTime?.format(LocalDateTime.Format { byUnicodePattern(pattern) })
}
