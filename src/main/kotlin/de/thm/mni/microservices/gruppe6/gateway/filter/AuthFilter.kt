package de.thm.mni.microservices.gruppe6.gateway.filter

import de.thm.mni.microservices.gruppe6.gateway.security.JwtService
import de.thm.mni.microservices.gruppe6.lib.exception.ServiceException
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * Implements an authorization filter to check the incoming jwt.
 */
@Component
class AuthFilter(
    private val jwtService: JwtService
    ): AbstractGatewayFilterFactory<AuthFilter.Config>(Config::class.java) {

        private val logger = LoggerFactory.getLogger(AuthFilter::class.java)

        override fun apply(config: Config): GatewayFilter {
            return GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
                Mono.just(exchange).doOnNext {
                    jwtService.authorize(it)
                }.onErrorResume { exception ->
                    logger.debug("Exception on verifying JWT and obtaining userId", exception)
                    Mono.error(ServiceException(HttpStatus.UNAUTHORIZED, "You are not authorized."))
                }.flatMap(chain::filter)
            }
        }

        class Config {
            var name: String = "AuthFilter"
        }
    }
