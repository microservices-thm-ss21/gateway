package de.thm.mni.microservices.gruppe6.gateway.gateways

import de.thm.mni.microservices.gruppe6.gateway.endpoints.IssueEndpoint
import de.thm.mni.microservices.gruppe6.gateway.endpoints.UserEndpoint
import de.thm.mni.microservices.gruppe6.gateway.filter.UserIsMemberFilter
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserGateway {

    @Bean
    fun userRouteLocator(builder: RouteLocatorBuilder, userIsMemberFilter: UserIsMemberFilter): RouteLocator {
        return builder.routes {
            route {
                path("/${UserEndpoint.BASE.url}/**")
                uri(UserEndpoint.SERVICE.url)
            }
        }
    }

}
