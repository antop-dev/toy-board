package org.antop.admin.common.jooq

import org.antop.admin.category.CategoryStatus
import org.jooq.impl.EnumConverter

class CategoryStatusConverter :
    EnumConverter<String, CategoryStatus>(
        String::class.java,
        CategoryStatus::class.java,
    ) {
    override fun to(u: CategoryStatus?): String? = u?.code
}
