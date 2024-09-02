package com.tencent.devops.schedule.api

import com.tencent.devops.schedule.constants.SCHEDULE_RPC_AUTH_HEADER
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class RpcAuthWebFilter(
    private val accessToken: String,
) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val requestToken = exchange.request.headers.getFirst(SCHEDULE_RPC_AUTH_HEADER).orEmpty()
        if (accessToken != requestToken) {
            logger.warn("Authenticate failed, $SCHEDULE_RPC_AUTH_HEADER[$requestToken] is invalid.")
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            return exchange.response.setComplete()
        }
        return chain.filter(exchange)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RpcAuthWebFilter::class.java)
    }
}
