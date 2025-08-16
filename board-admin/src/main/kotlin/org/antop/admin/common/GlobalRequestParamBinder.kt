package org.antop.admin.common

import org.springframework.beans.propertyeditors.StringTrimmerEditor
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.InitBinder

/**
 * 모든 컨트롤러에 공통으로 적용되는 요청 파라미터 처리를 정의하는 클래스.
 * Spring MVC의 @ControllerAdvice를 사용하여 작성되었으며,
 * 요청 파라미터에 대한 변환 및 처리를 설정한다.
 *
 * 이 클래스는 String 타입의 파라미터 값에서 공백을 제거하도록 커스터마이징된 에디터를 등록한다.
 */
@ControllerAdvice
class GlobalRequestParamBinder {
    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(String::class.java, StringTrimmerEditor(true))
    }
}
