package de.thm.mni.microservices.gruppe6.gateway.filter

import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class ProjectFilter : GatewayFilterFactory<ProjectFilter.Config> {
    class Config(var name: String = "DemoGatewayFilter")

    companion object {
        const val UUID = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}"
    }


    override fun newConfig() : Config{
        return Config("DemoGatewayFilter")
    }

    override fun getConfigClass(): Class<Config> {
        return Config::class.java
    }
    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            chain.filter(exchange)
                .then(Mono.just(exchange))
                .map {
                    println("headers: ${it.attributes}")
                }.then()
        }
    }
}
