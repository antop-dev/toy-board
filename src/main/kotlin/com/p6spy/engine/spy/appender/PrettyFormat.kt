package com.p6spy.engine.spy.appender

import com.github.vertical_blank.sqlformatter.SqlFormatter

class PrettyFormat : MessageFormattingStrategy {
    override fun formatMessage(
        connectionId: Int,
        now: String,
        elapsed: Long,
        category: String,
        prepared: String,
        sql: String,
        url: String,
    ): String =
        buildString {
            append("took ")
            append(elapsed)
            append("ms | ")
            append(category)
            append(url)
            if (sql.isNotBlank()) {
                append("\n")
                append(formatSql(sql))
            }
        }

    private fun formatSql(sql: String) = SqlFormatter.format(sql)
}
