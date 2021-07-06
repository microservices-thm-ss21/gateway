package de.thm.mni.microservices.gruppe6.gateway

import de.thm.mni.microservices.gruppe6.gateway.endpoints.ProjectEndpoint
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.context.annotation.Bean
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.routes

@SpringBootApplication
class GatewayApplication {
    @Bean
    fun customRouteLocator(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes {
            route {
                path("/${ProjectEndpoint.BASE.url}/**")
                uri(ProjectEndpoint.SERVICE.url)
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args)
}


