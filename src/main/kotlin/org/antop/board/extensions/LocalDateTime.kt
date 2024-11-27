package org.antop.board.extensions

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.ocpsoft.prettytime.PrettyTime

/**
 * 현재 일시
 */
fun LocalDateTime.Companion.now(): LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

/**
 * 현재 시간 대비 경과 시간으로 변환한다. 예) “13분 전”
 */
fun LocalDateTime.pretty(): String = PrettyTime().format(this.toJavaLocalDateTime())
