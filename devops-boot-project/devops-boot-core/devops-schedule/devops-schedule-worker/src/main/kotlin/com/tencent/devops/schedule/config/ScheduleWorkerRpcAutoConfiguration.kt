package com.tencent.devops.schedule.config

import com.tencent.devops.schedule.api.RpcAuthRequestExchangeFilterFunction
import com.tencent.devops.schedule.api.ServerRpcClient
import com.tencent.devops.schedule.remote.RemoteServerRpcClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration(proxyBeanMethods = false)
class ScheduleWorkerRpcAutoConfiguration {

    @Bean(SERVER_WEB_CLIENT)
    @ConditionalOnMissingBean(name = [SERVER_WEB_CLIENT])
    fun webClient(
        scheduleWorkerProperties: ScheduleWorkerProperties,
        builder: WebClient.Builder,
    ): WebClient {
        val accessToken = scheduleWorkerProperties.server.accessToken
        return builder
            .filter(RpcAuthRequestExchangeFilterFunction(accessToken))
            .build()
    }

    @Bean
    @ConditionalOnMissingBean
    fun serverRpcClient(
        scheduleWorkerProperties: ScheduleWorkerProperties,
        @Qualifier(SERVER_WEB_CLIENT)
        webClient: WebClient,
    ): ServerRpcClient {
        return RemoteServerRpcClient(scheduleWorkerProperties, webClient)
    }

    companion object {
        const val SERVER_WEB_CLIENT = "serverWebClient"
    }
}
