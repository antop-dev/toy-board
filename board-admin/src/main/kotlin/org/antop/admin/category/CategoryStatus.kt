package org.antop.admin.category

enum class CategoryStatus(
    val code: String,
    val korName: String,
) {
    ACTIVE("1", "활성화"),
    INACTIVE("2", "비활성화"),
    ARCHIVED("3", "보관"),
    ;

    companion object {
        /**
         * 코드값으로 상태 찾기
         */
        fun from(code: String): CategoryStatus? = entries.firstOrNull { it.code == code }
    }
}
