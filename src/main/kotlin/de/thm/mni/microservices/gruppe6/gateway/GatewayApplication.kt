package de.thm.mni.microservices.gruppe6.gateway

import de.thm.mni.microservices.gruppe6.gateway.endpoints.IssueEndpoint
import de.thm.mni.microservices.gruppe6.gateway.endpoints.ProjectEndpoint
import de.thm.mni.microservices.gruppe6.gateway.endpoints.UserEndpoint
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.context.annotation.Bean
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.web.bind.annotation.CrossOrigin

@SpringBootApplication
class GatewayApplication {
    @Bean
    @CrossOrigin
    fun customRouteLocator(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes {
            route {
                path("/${ProjectEndpoint.BASE.url}/**")
                uri(ProjectEndpoint.SERVICE.url)
            }
            route {
                path("/${IssueEndpoint.BASE.url}/**")
                uri(IssueEndpoint.SERVICE.url)
            }
            route {
                path("/${UserEndpoint.BASE.url}/**")
                uri(UserEndpoint.SERVICE.url)
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args)
}


