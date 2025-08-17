package org.antop.admin.common.jpa

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

/**
 * 엔터티의 생성 및 수정 정보를 자동으로 관리하는 데 사용되는 추상 클래스
 * 엔터티의 생성자, 생성 시각, 수정자, 수정 시각 데이터를 추적
 * `@MappedSuperclass`와 `@EntityListeners`를 통해 Audit 기능 제공
 * Spring Data JPA의 Auditing 기능 활용
 *
 * - `createdBy`: 생성한 사용자의 ID를 기록
 * - `createdAt`: 엔터티 생성 시각 기록
 * - `modifiedBy`: 마지막으로 수정한 사용자의 ID를 기록
 * - `modifiedAt`: 마지막 수정 시각 기록
 *
 * 필드 값은 외부에서 직접 변경할 수 없으며, Auditing에 의해 자동으로 설정됨
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class Auditable {
    @CreatedBy
    var createdBy: Long = 0L
        protected set

    @CreatedDate
    @Column(name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.now()
        protected set

    @LastModifiedBy
    @Column(name = "modified_by")
    var modifiedBy: Long? = null
        protected set

    @LastModifiedDate
    @Column(name = "modified_at")
    var modifiedAt: LocalDateTime? = null
        protected set
}
