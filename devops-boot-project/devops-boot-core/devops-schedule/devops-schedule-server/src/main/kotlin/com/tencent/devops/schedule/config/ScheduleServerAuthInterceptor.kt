package com.tencent.devops.schedule.config

import com.tencent.devops.api.pojo.Response
import com.tencent.devops.schedule.constants.SCHEDULE_API_AUTH_HEADER
import com.tencent.devops.schedule.utils.JwtUtils
import com.tencent.devops.utils.jackson.JsonUtils
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

/**
 * 调度中心认证拦截器
 */
class ScheduleServerAuthInterceptor(
    authProperties: ScheduleServerProperties.ScheduleServerAuthProperties,
) : WebFilter {

    private val signingKey = JwtUtils.createSigningKey(authProperties.secretKey)
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        if (exchange.request.method == HttpMethod.OPTIONS) {
            return chain.filter(exchange)
        }
        val jwtToken = exchange.request.headers.getFirst(SCHEDULE_API_AUTH_HEADER).orEmpty()
        return try {
            JwtUtils.validateToken(signingKey, jwtToken).body.subject
            chain.filter(exchange)
        } catch (exception: Exception) {
            logger.warn("Authenticate failed, $SCHEDULE_API_AUTH_HEADER[$jwtToken] is invalid.")
            val response = exchange.response
            response.headers.contentType = APPLICATION_JSON
            response.statusCode = HttpStatus.UNAUTHORIZED
            val body = Response.fail(HttpStatus.UNAUTHORIZED.value(), "认证失败")
            val bodyBuf = response.bufferFactory().wrap(JsonUtils.objectMapper.writeValueAsBytes(body))
            response.writeWith(Mono.just(bodyBuf))
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ScheduleServerAuthInterceptor::class.java)
    }
}
