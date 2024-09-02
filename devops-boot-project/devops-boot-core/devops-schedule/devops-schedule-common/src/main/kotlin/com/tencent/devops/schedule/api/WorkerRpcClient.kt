package com.tencent.devops.schedule.api

import com.tencent.devops.schedule.pojo.ScheduleResponse
import com.tencent.devops.schedule.pojo.trigger.TriggerParam
import reactor.core.publisher.Mono

/**
 * 远程调用客户端
 */
interface WorkerRpcClient {

    /**
     * 运行任务
     */
    fun runJob(param: TriggerParam): Mono<ScheduleResponse>
}
