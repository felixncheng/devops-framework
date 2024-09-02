package com.tencent.devops.schedule.remote

import com.tencent.devops.schedule.api.ServerRpcClient
import com.tencent.devops.schedule.config.ScheduleWorkerProperties
import com.tencent.devops.schedule.constants.RPC_HEART_BEAT
import com.tencent.devops.schedule.constants.RPC_SUBMIT_RESULT
import com.tencent.devops.schedule.constants.SERVER_RPC_V1
import com.tencent.devops.schedule.pojo.job.JobExecutionResult
import com.tencent.devops.schedule.pojo.trigger.HeartBeatParam
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class RemoteServerRpcClient(
    workerProperties: ScheduleWorkerProperties,
    private val webClient: WebClient,
) : ServerRpcClient {

    private val serverUrl = normalizeUrl(workerProperties.server)

    override fun submitResult(result: JobExecutionResult): Mono<Void> {
        return webClient.post()
            .uri(serverUrl + RPC_SUBMIT_RESULT)
            .bodyValue(result)
            .retrieve()
            .bodyToMono(Void::class.java)
    }

    override fun heartBeat(param: HeartBeatParam): Mono<Void> {
        return webClient.post()
            .uri(serverUrl + RPC_HEART_BEAT)
            .bodyValue(param)
            .retrieve()
            .bodyToMono(Void::class.java)
    }

    private fun normalizeUrl(server: ScheduleWorkerProperties.ScheduleWorkerServerProperties): String {
        val base = server.address.trim().trimEnd('/')
        return "$base$SERVER_RPC_V1"
    }
}
