package com.tencent.devops.webflux.filter

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class WebFilterConfiguration {
    @Autowired(required = false)
    fun setConfigurers(configurers: List<CustomWebFluxConfigurer>, beanFactory: ConfigurableBeanFactory) {
        val registry = WebFilterRegistry()
        configurers.forEach {
            it.addWebFilters(registry)
        }
        registry.getWebFilters().forEachIndexed() { idx, it ->
            beanFactory.registerSingleton(it.javaClass.simpleName + idx, it)
        }
    }
}
