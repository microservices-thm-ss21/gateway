package de.thm.mni.microservices.gruppe6.gateway.gateways

import de.thm.mni.microservices.gruppe6.gateway.endpoints.ProjectEndpoint
import de.thm.mni.microservices.gruppe6.gateway.filter.AuthFilter
import de.thm.mni.microservices.gruppe6.gateway.filter.UserIsMemberFilter
import de.thm.mni.microservices.gruppe6.gateway.message.GatewayProjectDTO
import de.thm.mni.microservices.gruppe6.gateway.security.JwtService
import de.thm.mni.microservices.gruppe6.lib.classes.projectService.ProjectDTO
import de.thm.mni.microservices.gruppe6.lib.classes.userService.User
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Configuration
class ProjectGateway(private val authFilter: AuthFilter) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    val user: User = User(
        UUID.fromString("a443ffd0-f7a8-44f6-8ad3-87acd1e91042"),
        "Peter_Zwegat",
        "password",
        "Peter",
        "Zwegat",
        "peter.zwegat@mni.thm.de",
        LocalDate.now(),
        LocalDateTime.now(),
        "normal",
        null
    )

    @Bean
    fun projectRouteLocator(builder: RouteLocatorBuilder, userIsMemberFilter: UserIsMemberFilter, jwtService: JwtService): RouteLocator {
        return builder.routes {
            route {
                // getAllProjects, getProject, getAllProjectsOfUser, getMembers
                path(
                    "/${ProjectEndpoint.BASE.url}",
                    "/${ProjectEndpoint.BASE.url}/*",
                    "/${ProjectEndpoint.BASE.url}/user/*",
                    "/${ProjectEndpoint.BASE.url}/*/members"
                ).and().predicate { exchange ->
                    logger.debug("http: ${exchange.request.method} | predicate get routes of projectService: ${exchange.request.method == HttpMethod.GET}")
                    exchange.request.method == HttpMethod.GET
                }
                filters {
                    filter(authFilter.apply(AuthFilter.Config()))
                }
                uri(ProjectEndpoint.SERVICE.url)
            }
            route {
                // deleteProject, updateProject, deleteMembers, updateMembers
                path(
                        "/${ProjectEndpoint.BASE.url}/{projectId}/*"
                )
                predicate { exchange ->
                    logger.debug("http: ${exchange.request.method} | predicate delete and update routes of projectService: ${exchange.request.method == HttpMethod.PUT || exchange.request.method == HttpMethod.DELETE}")
                    exchange.request.method == HttpMethod.PUT || exchange.request.method == HttpMethod.DELETE
                }
                filters {
                    filter(authFilter.apply(AuthFilter.Config()))
                    filter { exchange, chain ->
                        val user = jwtService.authorize(exchange)
                        val modifiedRequest = exchange.request
                                .mutate().path("${exchange.request.path}/user/${user.id}").build()
                        logger.debug("new path: ${modifiedRequest.path} ")
                        logger.debug("${exchange.request.path}")
                        chain.filter(exchange.mutate().request(modifiedRequest).build())
                    }
                }
                uri(ProjectEndpoint.SERVICE.url)
            }
            route {
                // createProject
                path(
                    "/${ProjectEndpoint.BASE.url}"
                )
                predicate { exchange ->
                    logger.debug("http: ${exchange.request.method} | predicate post routes of projectService: ${exchange.request.method == HttpMethod.POST}")
                    exchange.request.method == HttpMethod.POST
                }
                filters {
                    filter(authFilter.apply(AuthFilter.Config()))
                    modifyRequestBody(
                        GatewayProjectDTO::class.java, ProjectDTO::class.java,
                        MediaType.APPLICATION_JSON_VALUE
                    ) {
                        _, projectDTO -> Mono.just(ProjectDTO(projectDTO.name, user.id, projectDTO.members)) }
                    }
                uri(ProjectEndpoint.SERVICE.url)
            }

            route {
                // createMembers
                path(
                        "/${ProjectEndpoint.BASE.url}/*/members/user/*"
                )
                predicate { exchange ->
                    logger.debug("http: ${exchange.request.method} | predicate post routes of projectService: ${exchange.request.method == HttpMethod.POST}")
                    exchange.request.method == HttpMethod.POST
                }
                filters {
                    filter(authFilter.apply(AuthFilter.Config()))
                }
                uri(ProjectEndpoint.SERVICE.url)
            }

            //@toDo~~~~~~~~~~~~ Not tested ~~~~~~~~~~~~~
//            route {
//                path(
//                    "/${ProjectEndpoint.BASE.url}/{projectId}/members"
//                ).and()
//                    .predicate { exchange ->
//                        exchange.request.method == HttpMethod.GET
//                    }
//                    .filters { f ->
//                        f.filter(userIsMemberFilter.apply(userIsMemberFilter.customConfig(user)))
//                    }
//                uri(ProjectEndpoint.SERVICE.url)
//            }
        }
    }
}
