package com.tencent.devops.webflux.filter

import org.springframework.web.reactive.config.WebFluxConfigurer

interface CustomWebFluxConfigurer : WebFluxConfigurer {
    fun addWebFilters(registry: WebFilterRegistry) {}
}
