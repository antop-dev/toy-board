package org.antop.board.common.exposed

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table

/**
 * Set<String> ↔︎ JSON 타입 매핑 (내부는 [])
 */
class JsonArrayColumnType : ColumnType<Set<String>>() {
    override fun sqlType(): String = "longtext"

    override fun valueFromDB(value: Any): Set<String> =
        when (value) {
            is String -> Json.decodeFromString<Set<String>>(value)
            else -> setOf()
        }

    override fun notNullValueToDB(value: Set<String>): Any = nonNullValueToString(value)

    override fun nonNullValueToString(value: Set<String>): String = Json.encodeToString(value)
}

fun Table.jsonArray(name: String): Column<Set<String>> = registerColumn(name, JsonArrayColumnType())
