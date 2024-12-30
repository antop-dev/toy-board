package org.antop.board.common.exposed

import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.ExpressionWithColumnType
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.QueryBuilder
import org.jetbrains.exposed.sql.stringParam

/**
 * JSON_CONTAINS 펑션을 사용하여 검색 쿼리 생성
 *
 * WHERE JSON_CONTAINS([expr], 'one', '["[v]"]')
 */
class JsonContainsOp(
    private val expr: Expression<*>,
    private val v: String,
) : Op<Boolean>() {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        val param = stringParam("[\"$v\"]")
        queryBuilder {
            append("JSON_CONTAINS(")
            append(expr)
            append(", ")
            append(param)
            append(")")
        }
    }
}

infix fun <T> ExpressionWithColumnType<T>.jsonContains(keyword: String) = JsonContainsOp(this, keyword)
