package com.tencent.devops.schedule.api

import com.tencent.devops.schedule.constants.SCHEDULE_RPC_AUTH_HEADER
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import reactor.core.publisher.Mono

class RpcAuthRequestExchangeFilterFunction(
    private val accessToken: String,
) : ExchangeFilterFunction {
    override fun filter(request: ClientRequest, next: ExchangeFunction): Mono<ClientResponse> {
        val newRequest = ClientRequest.from(request)
            .header(SCHEDULE_RPC_AUTH_HEADER, accessToken)
            .build()
        return next.exchange(newRequest)
    }
}
