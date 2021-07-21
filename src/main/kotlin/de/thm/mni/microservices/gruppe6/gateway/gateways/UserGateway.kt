package de.thm.mni.microservices.gruppe6.gateway.gateways

import de.thm.mni.microservices.gruppe6.gateway.endpoints.UserEndpoint
import de.thm.mni.microservices.gruppe6.gateway.filter.AuthFilter
import de.thm.mni.microservices.gruppe6.gateway.filter.UserIsMemberFilter
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserGateway(private val authFilter: AuthFilter) {

    @Bean
    fun userRouteLocator(builder: RouteLocatorBuilder, userIsMemberFilter: UserIsMemberFilter): RouteLocator {
        return builder.routes {
            route {
                path("/${UserEndpoint.BASE.url}/**")
                filters {
                    filter(authFilter.apply(AuthFilter.Config()))
                }
                uri(UserEndpoint.SERVICE.url)
            }
        }
    }

}
