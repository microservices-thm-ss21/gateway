package de.thm.mni.microservices.gruppe6.gateway.gateways

import de.thm.mni.microservices.gruppe6.gateway.endpoints.IssueEndpoint
import de.thm.mni.microservices.gruppe6.gateway.endpoints.NewsEndpoint
import de.thm.mni.microservices.gruppe6.gateway.endpoints.ProjectEndpoint
import de.thm.mni.microservices.gruppe6.gateway.endpoints.UserEndpoint
import de.thm.mni.microservices.gruppe6.gateway.filter.AuthFilter
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Gateway(private val authFilter: AuthFilter) {

    @Bean
    fun routeLocator(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes {
            route { // login
                path("/login")
                uri(UserEndpoint.SERVICE.url)
            }
            route { // ProjectService
                path("/${ProjectEndpoint.BASE.url}/**")
                    .filters { f ->
                        f.filter(authFilter.apply(AuthFilter.Config()))
                    }
                uri(ProjectEndpoint.SERVICE.url)
            }
            route { // IssueService
                path("/${IssueEndpoint.BASE.url}/**")
                    .filters { f ->
                        f.filter(authFilter.apply(AuthFilter.Config()))
                    }
                uri(IssueEndpoint.SERVICE.url)
            }
            route { // UserEndpoint
                path("/${UserEndpoint.BASE.url}/**")
                    .filters { f ->
                        f.filter(authFilter.apply(AuthFilter.Config()))
                    }
                uri(UserEndpoint.SERVICE.url)
            }
            route { // NewsEndpoint
                path("/${NewsEndpoint.BASE.url}/**")
                    .filters { f ->
                        f.filter(authFilter.apply(AuthFilter.Config()))
                    }
                uri(NewsEndpoint.SERVICE.url)
            }
        }
    }
}
