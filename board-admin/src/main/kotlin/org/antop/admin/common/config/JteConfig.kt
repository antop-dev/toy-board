package org.antop.admin.common.config

import gg.jte.ContentType
import gg.jte.TemplateEngine
import gg.jte.resolve.ResourceCodeResolver
import gg.jte.springframework.boot.autoconfigure.JteConfigurationException
import gg.jte.springframework.boot.autoconfigure.JteProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.file.Paths

@Configuration
class JteConfig {
    /**
     * [gg.jte.springframework.boot.autoconfigure.JteAutoConfiguration]에서 제공하는 TemplateEngine은 파일 디엙터리 기반으로 작동한다.
     * 그런데 서브모듈에서 구조에서 제대로 동작하지 않는다.
     * 따라서, [ResourceCodeResolver]를 사용하는 [TemplateEngine]을 직접 생성한다.
     */
    @Bean
    fun jteTemplateEngine(jteProperties: JteProperties): TemplateEngine {
        println()

        if (jteProperties.isDevelopmentMode && jteProperties.usePreCompiledTemplates()) {
            throw JteConfigurationException("You can't use development mode and precompiledTemplates together")
        }
        if (jteProperties.usePreCompiledTemplates()) {
            // Templates will need to be compiled by the maven/gradle build task
            return TemplateEngine.createPrecompiled(ContentType.Html)
        }
        if (jteProperties.isDevelopmentMode) {
            val codeResolver = ResourceCodeResolver(jteProperties.templateLocation)
            return TemplateEngine.create(
                codeResolver,
                Paths.get("jte-classes"),
                ContentType.Html,
                javaClass.getClassLoader(),
            )
        }
        throw JteConfigurationException("You need to either set gg.jte.usePrecompiledTemplates or gg.jte.developmentMode to true ")
    }
}
