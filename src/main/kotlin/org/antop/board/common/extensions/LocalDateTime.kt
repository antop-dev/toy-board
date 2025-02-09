package org.antop.board.common.extensions

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.ocpsoft.prettytime.PrettyTime

/**
 * 현재 일시
 */
fun LocalDateTime.Companion.now(): LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

/**
 * 당일일 때와 아닐 때 다른 포멧
 */
@OptIn(FormatStringsInDatetimeFormats::class)
fun LocalDateTime.pretty(): String {
    val now = LocalDateTime.now()
    return if (date == now.date) {
        PrettyTime().format(this.toJavaLocalDateTime())
    } else {
        val format = LocalDateTime.Format { byUnicodePattern("yyyy.MM.dd") }
        return this.format(format)
    }
}
