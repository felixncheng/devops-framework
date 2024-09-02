package com.tencent.devops.schedule.config

import com.tencent.devops.schedule.api.RpcAuthWebFilter
import com.tencent.devops.schedule.constants.WORKER_RPC_V1
import com.tencent.devops.webflux.filter.CustomWebFluxConfigurer
import com.tencent.devops.webflux.filter.WebFilterRegistry

/**
 * Schedule server api 配置
 */
class ScheduleWorkerWebAuthConfigurer(
    scheduleWorkerProperties: ScheduleWorkerProperties,
) : CustomWebFluxConfigurer {

    private val serverProperties = scheduleWorkerProperties.server

    override fun addWebFilters(registry: WebFilterRegistry) {
        registry.addWebFilter(RpcAuthWebFilter(serverProperties.accessToken))
            .addPathPatterns("$WORKER_RPC_V1/**")
    }
}
