package com.tencent.devops.webflux

import com.tencent.devops.webflux.filter.WebFilterConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration(proxyBeanMethods = false)
@Import(WebFilterConfiguration::class)
@AutoConfigureBefore(WebFluxAutoConfiguration::class)
class WebFluxAutoConfiguration
