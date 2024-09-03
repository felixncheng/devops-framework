package com.tencent.devops.schedule.config

import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration(proxyBeanMethods = false)
@ConditionalOnSingleCandidate(DiscoveryClient::class)
@AutoConfigureBefore(ScheduleWorkerRpcAutoConfiguration::class)
@AutoConfigureAfter(
    name = [
        "org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration",
    ],
)
class ScheduleWorkerDiscoveryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnSingleCandidate(DiscoveryClient::class)
    @ConditionalOnProperty(
        prefix = ScheduleWorkerProperties.PREFIX,
        name = ["mode"],
        havingValue = "DISCOVERY",
        matchIfMissing = false,
    )
    @LoadBalanced
    fun webClientBuilder(customizerProvider: ObjectProvider<WebClientCustomizer>): WebClient.Builder {
        val builder = WebClient.builder()
        customizerProvider.orderedStream().forEach { it.customize(builder) }
        return builder
    }
}
