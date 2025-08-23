package org.antop.admin.category.model

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

/**
 * 데이터베이스의 카테고리 상태와 애플리케이션 내의 `CategoryStatus` enum 간의 변환을 처리하는 클래스
 *
 * 역할:
 * - `CategoryStatus` enum을 데이터베이스에 저장할 문자열 값으로 변환
 * - 데이터베이스에서 조회한 문자열 값을 `CategoryStatus` enum으로 변환
 *
 * 특징:
 * - JPA의 `AttributeConverter`를 구현하여 자동으로 변환 과정 처리
 * - `@Converter(autoApply = true)`를 통해 엔터티 필드에 별도 설정 없이 자동 적용
 *
 * 동작:
 * - `convertToDatabaseColumn`: enum의 `code` 값을 데이터베이스 컬럼 값으로 전달
 * - `convertToEntityAttribute`: 데이터베이스 컬럼 값을 기반으로 `CategoryStatus` enum 생성. 잘못된 값인 경우 예외 발생
 */
@Converter(autoApply = true)
class CategoryStatusConverter : AttributeConverter<CategoryStatus, String> {
    override fun convertToDatabaseColumn(attribute: CategoryStatus?) = attribute?.code

    override fun convertToEntityAttribute(dbData: String?) = dbData?.let { code -> CategoryStatus.from(code) }
}
