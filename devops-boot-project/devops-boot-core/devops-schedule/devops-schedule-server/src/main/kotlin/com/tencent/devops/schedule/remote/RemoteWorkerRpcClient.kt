package com.tencent.devops.schedule.remote

import com.tencent.devops.schedule.api.WorkerRpcClient
import com.tencent.devops.schedule.constants.RPC_RUN_JOB
import com.tencent.devops.schedule.constants.WORKER_RPC_V1
import com.tencent.devops.schedule.pojo.ScheduleResponse
import com.tencent.devops.schedule.pojo.trigger.TriggerParam
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class RemoteWorkerRpcClient(
    private val webClient: WebClient,
) : WorkerRpcClient {
    override fun runJob(param: TriggerParam): Mono<ScheduleResponse> {
        val workerAddress = param.workerAddress.orEmpty()
        require(workerAddress.isNotBlank())
        val runJobAddress = workerAddress + WORKER_RPC_V1 + RPC_RUN_JOB
        return webClient.post()
            .uri(runJobAddress)
            .bodyValue(param)
            .retrieve()
            .bodyToMono()
    }
}
