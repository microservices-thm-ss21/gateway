package de.thm.mni.microservices.gruppe6.gateway

import de.thm.mni.microservices.gruppe6.gateway.endpoints.IssueEndpoint
import de.thm.mni.microservices.gruppe6.gateway.endpoints.ProjectEndpoint
import de.thm.mni.microservices.gruppe6.gateway.endpoints.UserEndpoint
import de.thm.mni.microservices.gruppe6.gateway.model.User
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.context.annotation.Bean
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@SpringBootApplication
class GatewayApplication {
    val user : User = User(UUID.fromString("a443ffd0-f7a8-44f6-8ad3-87acd1e91042"), "Peter_Zwegat", "Peter", "Zwegat", "peter.zwegat@mni.thm.de", LocalDate.now(), LocalDateTime.now(), "normal", null)

    @Bean
    fun customRouteLocator(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes {
            // project service
            route {
                path("/${ProjectEndpoint.BASE.url}/user")
                filters { rewritePath("user","users/${user.id}") }
                uri(ProjectEndpoint.SERVICE.url)
            }
            route {
                path("/${ProjectEndpoint.BASE.url}/projects/*/members")
                filters {

                    rewritePath("*","users/${user.id}")

                }
                uri(ProjectEndpoint.SERVICE.url)
            }
            // issue service
            route {
                path("/${IssueEndpoint.BASE.url}/**")
                uri(IssueEndpoint.SERVICE.url)
            }
            // user service
            route {
                path("/${UserEndpoint.BASE.url}/**")
                uri(UserEndpoint.SERVICE.url)
            }
            // news service
        }
    }
}

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args)
}


