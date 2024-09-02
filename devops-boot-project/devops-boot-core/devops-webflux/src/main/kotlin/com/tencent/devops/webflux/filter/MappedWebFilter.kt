package com.tencent.devops.webflux.filter

import org.springframework.http.server.PathContainer
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.util.AntPathMatcher
import org.springframework.util.ObjectUtils
import org.springframework.util.PathMatcher
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import org.springframework.web.util.UrlPathHelper
import org.springframework.web.util.pattern.PathPattern
import org.springframework.web.util.pattern.PathPatternParser
import org.springframework.web.util.pattern.PatternParseException
import reactor.core.publisher.Mono

class MappedWebFilter(
    includePatterns: List<String>,
    excludePatterns: List<String>,
    private val webFilter: WebFilter,
) : WebFilter {
    private val includePatterns0: List<PatternAdapter>?
    private val excludePatterns0: List<PatternAdapter>?
    private val pathMatcher = defaultPathMatcher

    init {
        this.includePatterns0 = PatternAdapter.initPatterns(includePatterns, null)
        this.excludePatterns0 = PatternAdapter.initPatterns(excludePatterns, null)
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return if (matches(exchange.request)) {
            webFilter.filter(exchange, chain)
        } else {
            chain.filter(exchange)
        }
    }

    private fun matches(request: ServerHttpRequest): Boolean {
        val path = request.path.pathWithinApplication()
        if (!excludePatterns0.isNullOrEmpty()) {
            if (excludePatterns0.any { it.match(path, true, pathMatcher) }) {
                return false
            }
        }
        if (includePatterns0.isNullOrEmpty() ||
            includePatterns0.any { it.match(path, true, pathMatcher) }
        ) {
            return true
        }
        return false
    }

    companion object {
        private val defaultPathMatcher = AntPathMatcher()

        private class PatternAdapter(val patternString: String, parser: PathPatternParser?) {

            private val pathPattern: PathPattern? = initPathPattern(patternString, parser)

            fun match(path: Any, isPathContainer: Boolean, pathMatcher: PathMatcher): Boolean {
                var realPath = path
                if (isPathContainer) {
                    val pathContainer = path as PathContainer
                    if (pathPattern != null) {
                        return pathPattern.matches(pathContainer)
                    }
                    val lookupPath = pathContainer.value()
                    realPath = UrlPathHelper.defaultInstance.removeSemicolonContent(lookupPath)
                }
                return pathMatcher.match(patternString, realPath as String)
            }

            companion object {
                private fun initPathPattern(pattern: String, parser: PathPatternParser?): PathPattern? {
                    return try {
                        (parser ?: PathPatternParser.defaultInstance).parse(pattern)
                    } catch (ex: PatternParseException) {
                        null
                    }
                }

                fun initPatterns(patterns: List<String>, parser: PathPatternParser?): List<PatternAdapter>? {
                    return if (ObjectUtils.isEmpty(patterns)) {
                        null
                    } else {
                        patterns.map { PatternAdapter(it, parser) }
                    }
                }
            }
        }
    }
}
