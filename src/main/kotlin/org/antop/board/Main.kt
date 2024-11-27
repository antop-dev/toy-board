package org.antop.board

import kotlinx.datetime.LocalDateTime
import org.antop.board.extensions.now
import org.antop.board.extensions.pretty

fun main() {
    val date = LocalDateTime.now()
    println("date: $date")
    val pretty = date.pretty()
    println("pretty = $pretty")
}
