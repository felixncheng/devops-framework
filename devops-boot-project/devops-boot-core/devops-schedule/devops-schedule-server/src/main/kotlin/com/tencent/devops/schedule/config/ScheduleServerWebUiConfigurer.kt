package com.tencent.devops.schedule.config

import org.springframework.web.reactive.config.ResourceHandlerRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer

/**
 * Schedule server ui访问配置
 */
class ScheduleServerWebUiConfigurer(
    scheduleServerProperties: ScheduleServerProperties,
) : WebFluxConfigurer {

    private val contextPath = scheduleServerProperties.contextPath.trimEnd('/')

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("$contextPath/ui/**")
            .addResourceLocations("classpath:/frontend/dist/")
            .resourceChain(false)
    }

    /*    override fun addViewControllers(registry: ViewControllerRegistry) {
            registry.addViewController("$contextPath/")
                .setViewName("redirect:$contextPath/ui/")
            registry.addViewController("$contextPath/ui/")
                .setViewName("forward:$contextPath/ui/index.html")
        }*/
}
