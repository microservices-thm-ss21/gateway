package de.thm.mni.microservices.gruppe6.gateway.filter

import de.thm.mni.microservices.gruppe6.gateway.model.User
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class ProjectFilter : GatewayFilterFactory<ProjectFilter.Config> {
    class Config(var name: String = "DemoGatewayFilter", var user: User)

    private val logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        val UUID : Regex = Regex("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}")
    }


    fun customConfig(user: User) : Config{
        return Config("ProjectFilter", user)
    }

    override fun getConfigClass(): Class<Config> {
        return Config::class.java
    }
    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            chain.filter(exchange)
                .then(Mono.just(exchange))
                .map {
                    val uriVariables = ServerWebExchangeUtils.getUriTemplateVariables(exchange)
                    val projectId = uriVariables["projectId"]

                    if(!projectId!!.matches(UUID)){
                        throw ResponseStatusException(HttpStatus.BAD_REQUEST)
                    }

                    logger.debug("ProjectId: $projectId")
                    logger.debug("UserId: ${config.user.id}")



                }.then()
        }
    }
}
