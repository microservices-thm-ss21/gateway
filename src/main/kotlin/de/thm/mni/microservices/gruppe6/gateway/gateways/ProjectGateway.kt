package de.thm.mni.microservices.gruppe6.gateway.gateways

import de.thm.mni.microservices.gruppe6.gateway.endpoints.ProjectEndpoint
import de.thm.mni.microservices.gruppe6.gateway.filter.UserIsMemberFilter
import de.thm.mni.microservices.gruppe6.gateway.message.GatewayProjectDTO
import de.thm.mni.microservices.gruppe6.lib.classes.projectService.ProjectDTO
import de.thm.mni.microservices.gruppe6.lib.classes.userService.User
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Configuration
class ProjectGateway {

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
    fun projectRouteLocator(builder: RouteLocatorBuilder, userIsMemberFilter: UserIsMemberFilter): RouteLocator {
        return builder.routes {
            route {
                // getAllProjects, getProject, getAllProjectsOfUser
                path(
                    "/${ProjectEndpoint.BASE.url}",
                    "/${ProjectEndpoint.BASE.url}/*",
                    "/${ProjectEndpoint.BASE.url}/users/**"
                ).and().predicate { exchange ->
                    logger.debug("http: ${exchange.request.method} | predicate get routes of projectService: ${exchange.request.method == HttpMethod.GET}")
                    exchange.request.method == HttpMethod.GET
                }
                uri(ProjectEndpoint.SERVICE.url)
            }
            route {
                // deleteProject, updateProject
                path("/${ProjectEndpoint.BASE.url}/{projectId}").and()
                    .predicate { exchange ->
                        logger.debug("http: ${exchange.request.method} | predicate delete and update routes of projectService: ${exchange.request.method == HttpMethod.PUT || exchange.request.method == HttpMethod.DELETE}")
                        exchange.request.method == HttpMethod.PUT || exchange.request.method == HttpMethod.DELETE
                    }
                filters {
                    GatewayFilter { exchange, chain ->
                        val modifiedRequest = exchange.request
                            .mutate().path("${exchange.request.path}/users/${user.id}").build()
                        chain.filter(exchange.mutate().request(modifiedRequest).build())

                    }
                }
                uri(ProjectEndpoint.SERVICE.url)
            }
            route {
                // createProject
                path(
                    "/${ProjectEndpoint.BASE.url}"
                ).and()
                    .predicate { exchange ->
                        logger.debug("http: ${exchange.request.method} | predicate post routes of projectService: ${exchange.request.method == HttpMethod.POST}")
                        exchange.request.method == HttpMethod.POST
                    }
                filters {
                    modifyRequestBody(
                        GatewayProjectDTO::class.java, ProjectDTO::class.java,
                        MediaType.APPLICATION_JSON_VALUE
                    ) { _, projectDTO -> Mono.just(ProjectDTO(projectDTO.name, user.id, projectDTO.members)) }
                }
                uri(ProjectEndpoint.SERVICE.url)
            }

            //@toDo~~~~~~~~~~~~ Not tested ~~~~~~~~~~~~~

            route {
                path("/${ProjectEndpoint.BASE.url}/{projectId}/member")
                uri(ProjectEndpoint.SERVICE.url)
            }
            route {
                path(
                    "/${ProjectEndpoint.BASE.url}/{projectId}/members"
                )
                    .filters { f ->
                        f.filter(userIsMemberFilter.apply(userIsMemberFilter.customConfig(user)))
                    }
                uri(ProjectEndpoint.SERVICE.url)
            }
        }
    }
}
