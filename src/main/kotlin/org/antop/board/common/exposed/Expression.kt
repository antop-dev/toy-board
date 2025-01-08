package org.antop.board.common.exposed

import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.LikeEscapeOp
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like

/**
 * Table.column like "%${input}%" → Table.column contains input
 */
infix fun <T : String?> Expression<T>.contains(it: String): LikeEscapeOp = like("%$it%")
