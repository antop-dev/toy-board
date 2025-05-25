package org.antop.board.common.extensions

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.extensions.comma

class NumberTest {
    @Test
    fun comma() {
        assertThat(0.comma(), `is`("0"))
        assertThat(125.comma(), `is`("125"))
        assertThat(12345.comma(), `is`("12,345"))
        assertThat(12345L.comma(), `is`("12,345"))
        assertThat(12345.0.comma(), `is`("12,345"))
        assertThat(BigDecimal("1234567890").comma(), `is`("1,234,567,890"))
        assertThat(56789F.comma(), `is`("56,789"))
        assertThat(3000.toShort().comma(), `is`("3,000"))
        assertThat((-3000).comma(), `is`("-3,000"))
        assertThat(1234.56.comma(), `is`("1,234.56"))
    }
}
