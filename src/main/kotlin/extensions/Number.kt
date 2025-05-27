package extensions

import java.text.NumberFormat
import java.util.Locale

private val formatter: NumberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

/**
 * 이 함수는 숫자를 천 단위 구분 기호가 포함된 문자열로 변환
 *
 * ex) 1234 → "1,234"
 */
fun Number.comma(): String = formatter.format(this)
