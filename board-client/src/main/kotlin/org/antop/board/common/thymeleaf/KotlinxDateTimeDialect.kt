package org.antop.board.common.thymeleaf

import org.thymeleaf.context.IExpressionContext
import org.thymeleaf.dialect.AbstractDialect
import org.thymeleaf.dialect.IExpressionObjectDialect
import org.thymeleaf.expression.IExpressionObjectFactory

class KotlinxDateTimeDialect :
    AbstractDialect("KotlinxDateTimeDialect"),
    IExpressionObjectDialect {
    override fun getExpressionObjectFactory(): IExpressionObjectFactory =
        object : IExpressionObjectFactory {
            override fun getAllExpressionObjectNames() = setOf("datetimex")

            override fun buildObject(
                context: IExpressionContext?,
                expressionObjectName: String?,
            ) = KotlinxDateTimeExpression()

            override fun isCacheable(expressionObjectName: String) = true
        }
}
