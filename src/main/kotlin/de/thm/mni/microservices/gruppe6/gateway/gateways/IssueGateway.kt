package de.thm.mni.microservices.gruppe6.gateway.gateways

import de.thm.mni.microservices.gruppe6.gateway.endpoints.IssueEndpoint
import de.thm.mni.microservices.gruppe6.gateway.endpoints.ProjectEndpoint
import de.thm.mni.microservices.gruppe6.gateway.endpoints.UserEndpoint
import de.thm.mni.microservices.gruppe6.gateway.filter.UserIsMemberFilter
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class IssueGateway {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun issueRouteLocator(builder: RouteLocatorBuilder, userIsMemberFilter: UserIsMemberFilter): RouteLocator {
        return builder.routes {
            route {
                path("/${IssueEndpoint.BASE.url}/**")
                uri(IssueEndpoint.SERVICE.url)
            }
        }
    }
}
