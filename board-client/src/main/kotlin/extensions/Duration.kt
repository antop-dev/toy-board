package extensions

import java.time.Duration

/**
 * x시간 y분 z초로 표현
 */
fun Duration.koreanUnit(): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return buildString {
        if (hours > 0) append(hours).append("시간 ")
        if (minutes > 0) append(minutes).append("분 ")
        if (secs > 0 || isEmpty()) append(secs).append("초")
        trim()
    }
}
