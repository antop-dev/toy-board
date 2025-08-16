package org.antop.admin.common.exceptions

/**
 * 카테고리를 찾을 수 없을 때 발생하는 예외 클래스입니다.
 *
 * 이 예외는 요청한 카테고리가 데이터베이스에 존재하지 않을 경우 발생합니다.
 *
 * @property id 찾을 수 없는 카테고리의 ID
 */
class CategoryNotFoundException(
    /** 카테고리ID*/
    id: Long,
) : RuntimeException("카테고리($id)를 찾을 수 없습니다.")
