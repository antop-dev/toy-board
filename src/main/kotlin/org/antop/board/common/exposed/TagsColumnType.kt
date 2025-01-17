package org.antop.board.common.exposed

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.ExpressionWithColumnType
import org.jetbrains.exposed.sql.LikeEscapeOp
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.stringParam
import org.jetbrains.exposed.sql.vendors.currentDialect

/**
 * Set<String> ↔︎ JSON 타입 매핑 (내부는 [])
 */
class TagsColumnType(
    private val delimiter: String,
) : ColumnType<Set<String>>() {
    override fun sqlType(): String = currentDialect.dataTypeProvider.textType()

    override fun valueFromDB(value: Any): Set<String> =
        when (value) {
            is String -> value.split(delimiter).toSet()
            is Iterable<*> -> value.mapNotNull { it.toString() }.toSet()
            else -> throw IllegalArgumentException("Unexpected value: $value")
        }

    override fun valueToDB(value: Set<String>?): Any? =
        when (value) {
            is Collection<*> -> value.joinToString(delimiter) { it }
            null -> null
            else -> throw IllegalArgumentException("Unexpected value: $value")
        }

    override fun notNullValueToDB(value: Set<String>): Any = nonNullValueToString(value)

    override fun nonNullValueToString(value: Set<String>): String = value.joinToString(delimiter)
}

fun Table.tags(
    name: String,
    delimiter: String = " ",
): Column<Set<String>> = registerColumn(name, TagsColumnType(delimiter))

infix fun ExpressionWithColumnType<Set<String>?>.contains(v: String) = LikeEscapeOp(this, stringParam(v), true, null)
