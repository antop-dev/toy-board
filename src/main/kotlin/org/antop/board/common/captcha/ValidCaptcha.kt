package org.antop.board.common.captcha

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidCaptcha(
    val param: String = "captcha",
    val session: String = "captcha",
)
