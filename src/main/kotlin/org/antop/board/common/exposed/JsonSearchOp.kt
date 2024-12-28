package org.antop.board.common.exposed

import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.ExpressionWithColumnType
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.QueryBuilder
import org.jetbrains.exposed.sql.stringParam

/**
 * JSON_SEARCH 펑션을 사용하여 검색 쿼리 생성
 *
 * JSON_SEARCH([expr1], 'one', [expr2]) IS NOT NULL
 */
class JsonSearchOp(
    private val expr1: Expression<*>,
    private val expr2: Expression<*>,
) : Op<Boolean>() {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        queryBuilder {
            append("JSON_SEARCH(")
            append(expr1)
            append(", 'one', ")
            append(expr2)
            append(") IS NOT NULL")
        }
    }
}

infix fun <T> ExpressionWithColumnType<T>.jsonSearch(keyword: String) = JsonSearchOp(this, stringParam(keyword))
