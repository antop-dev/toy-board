package org.antop.board.common.extensions

import java.text.NumberFormat
import java.util.Locale

private val formatter: NumberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

/**
 * 1234 → "1,234"
 */
fun Number.comma(): String = formatter.format(this)
