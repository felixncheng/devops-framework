package com.tencent.devops.schedule.metrics

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(MetricsAutoConfiguration::class, SimpleMetricsExportAutoConfiguration::class)
@ConditionalOnBean(MeterRegistry::class)
class ScheduleServerMetricsAutoConfiguration {
    @Autowired
    fun bindServerToRegistry(registry: MeterRegistry) {
        ScheduleServerMetrics().bindTo(registry)
    }
}
