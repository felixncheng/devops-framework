package com.tencent.devops.schedule.config

import com.tencent.devops.schedule.api.RpcAuthWebFilter
import com.tencent.devops.schedule.constants.SERVER_API_V1
import com.tencent.devops.schedule.constants.SERVER_RPC_V1
import com.tencent.devops.webflux.filter.CustomWebFluxConfigurer
import com.tencent.devops.webflux.filter.WebFilterRegistry
import org.springframework.web.reactive.config.CorsRegistry

/**
 * Schedule server api 配置
 */
class ScheduleServerWebAuthConfigurer(
    scheduleServerProperties: ScheduleServerProperties,
) : CustomWebFluxConfigurer {

    private val contextPath = scheduleServerProperties.contextPath.trimEnd('/')
    private val authProperties = scheduleServerProperties.auth

    /**
     * 跨域访问配置
     */
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("$contextPath/**")
            .allowedOriginPatterns("*")
            .allowedMethods("*")
            .allowedHeaders("*")
            .allowCredentials(true)
    }

    override fun addWebFilters(registry: WebFilterRegistry) {
        registry.addWebFilter(RpcAuthWebFilter(authProperties.accessToken))
            .addPathPatterns("$contextPath$SERVER_RPC_V1/**")

        registry.addWebFilter(ScheduleServerAuthInterceptor(authProperties))
            .addPathPatterns("$contextPath$SERVER_API_V1/**")
            .excludePathPatterns(
                "$contextPath$SERVER_API_V1/",
                "$contextPath$SERVER_API_V1/user/info",
                "$contextPath$SERVER_API_V1/user/login",
                "$contextPath$SERVER_API_V1/user/logout",
                "$contextPath$SERVER_API_V1/dict/**",
            )
    }
}
