package org.antop.admin.common.jooq

/**
 * https://docs.spring.io/spring-data/jpa/reference/auditing.html
 */
fun interface AuditorAware<T> {
    fun getCurrentAuditor(): T?
}
