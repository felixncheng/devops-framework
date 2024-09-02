package com.tencent.devops.webflux.filter

import org.springframework.web.server.WebFilter

class WebFilterRegistration(private val webFilter: WebFilter) {
    private val includePatterns = mutableListOf<String>()
    private val excludePatterns = mutableListOf<String>()
    fun addPathPatterns(vararg patterns: String): WebFilterRegistration {
        includePatterns.addAll(patterns)
        return this
    }

    fun excludePathPatterns(vararg patterns: String): WebFilterRegistration {
        excludePatterns.addAll(patterns)
        return this
    }

    fun getWebFilter(): WebFilter {
        return MappedWebFilter(includePatterns, excludePatterns, webFilter)
    }
}
