package com.tencent.devops.schedule.metrics

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import io.micrometer.core.instrument.binder.MeterBinder
import java.util.concurrent.TimeUnit

class ScheduleServerMetrics : MeterBinder {

    override fun bindTo(registry: MeterRegistry) {
        Companion.registry = registry
    }

    companion object {
        private lateinit var registry: MeterRegistry
        fun recordTrigger(amount: Long, unit: TimeUnit) {
            getTimer("devops.schedule.server.trigger", "任务触发延迟").record(amount, unit)
        }

        private fun getTimer(metricName: String, description: String): Timer {
            return Timer.builder(metricName)
                .description(description)
                .register(registry)
        }
    }
}
