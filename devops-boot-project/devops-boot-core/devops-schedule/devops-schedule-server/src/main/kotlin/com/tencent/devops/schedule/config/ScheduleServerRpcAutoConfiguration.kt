package com.tencent.devops.schedule.config

import com.tencent.devops.schedule.api.RpcAuthRequestExchangeFilterFunction
import com.tencent.devops.schedule.api.WorkerRpcClient
import com.tencent.devops.schedule.remote.RemoteWorkerRpcClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration(proxyBeanMethods = false)
class ScheduleServerRpcAutoConfiguration {

    @Bean(WORKER_WEB_CLIENT)
    @ConditionalOnMissingBean(name = [WORKER_WEB_CLIENT])
    fun webClient(
        scheduleServerProperties: ScheduleServerProperties,
        builder: WebClient.Builder,
    ): WebClient {
        val accessToken = scheduleServerProperties.auth.accessToken
        return builder
            .filter(RpcAuthRequestExchangeFilterFunction(accessToken))
            .build()
    }

    @Bean
    @ConditionalOnMissingBean
    fun webClientBuilder(): WebClient.Builder {
        return WebClient.builder()
    }

    @Bean
    @ConditionalOnMissingBean
    fun serverRpcClient(
        scheduleWorkerProperties: ScheduleServerProperties,
        @Qualifier(WORKER_WEB_CLIENT)
        webClient: WebClient,
    ): WorkerRpcClient {
        return RemoteWorkerRpcClient(webClient)
    }

    companion object {
        const val WORKER_WEB_CLIENT = "workerWebClient"
    }
}
