package com.tencent.devops.webflux.filter

import org.springframework.web.server.WebFilter

class WebFilterRegistry {
    private val registrations = mutableListOf<WebFilterRegistration>()

    fun addWebFilter(webFilter: WebFilter): WebFilterRegistration {
        val registration = WebFilterRegistration(webFilter)
        registrations.add(registration)
        return registration
    }

    fun getWebFilters(): List<WebFilter> {
        return registrations.map { it.getWebFilter() }
    }
}
