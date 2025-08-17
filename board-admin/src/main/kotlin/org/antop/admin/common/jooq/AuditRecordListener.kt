package org.antop.admin.common.jooq

import org.jooq.RecordContext
import org.jooq.RecordListener
import java.time.LocalDateTime

/**
 * JOOQ Record 이벤트를 감지하여 생성일시, 생성자, 수정일시, 수정자와 같은 감사(audit) 정보를 자동으로 설정하는 리스너 클래스입니다.
 * 레코드가 생성될 때는 created_at, created_by를 설정하고
 * 레코드가 수정될 때는 modified_at, modified_by를 설정합니다.
 */
class AuditRecordListener(
    private val auditorAware: AuditorAware<Long>,
) : RecordListener {
    companion object {
        const val COLUMN_CREATED_BY = "created_by"
        const val COLUMN_CREATED_AT = "created_at"
        const val COLUMN_MODIFIED_BY = "modified_by"
        const val COLUMN_MODIFIED_AT = "modified_at"
    }

    override fun insertStart(ctx: RecordContext) {
        val record = ctx.record()
        record.field(COLUMN_CREATED_AT, LocalDateTime::class.java)?.let { field ->
            record.setValue(field, LocalDateTime.now())
        }
        record.field(COLUMN_CREATED_BY, Long::class.java)?.let { field ->
            record.setValue(field, auditorAware.getCurrentAuditor())
        }
    }

    override fun updateStart(ctx: RecordContext) {
        val record = ctx.record()
        record.field(COLUMN_MODIFIED_AT, LocalDateTime::class.java)?.let { field ->
            record.setValue(field, LocalDateTime.now())
        }
        record.field(COLUMN_MODIFIED_BY, Long::class.java)?.let { field ->
            record.setValue(field, auditorAware.getCurrentAuditor())
        }
    }
}
