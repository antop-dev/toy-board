package org.antop.board.member.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.site")
data class SiteProperties(
    val url: String,
)
