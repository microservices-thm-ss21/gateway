package de.thm.mni.microservices.gruppe6.gateway

import de.thm.mni.microservices.gruppe6.gateway.endpoints.IssueEndpoint
import de.thm.mni.microservices.gruppe6.gateway.endpoints.ProjectEndpoint
import de.thm.mni.microservices.gruppe6.gateway.endpoints.UserEndpoint
import de.thm.mni.microservices.gruppe6.gateway.filter.UserIsMemberFilter
import de.thm.mni.microservices.gruppe6.gateway.message.GatewayProjectDTO
import de.thm.mni.microservices.gruppe6.lib.classes.projectService.ProjectDTO
import de.thm.mni.microservices.gruppe6.lib.classes.userService.User
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.filter.HiddenHttpMethodFilter
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@SpringBootApplication
class GatewayApplication {
    val user: User = User(
        UUID.fromString("a443ffd0-f7a8-44f6-8ad3-87acd1e91042"),
        "Peter_Zwegat",
        "Peter",
        "Zwegat",
        "peter.zwegat@mni.thm.de",
        LocalDate.now(),
        LocalDateTime.now(),
        "normal",
        null
    )
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Override doFilterInternal to make HTTP-Methods delete and put work
     * Explanation: https://www.programmersought.com/article/38211750966/
     */
    @Bean
    fun hiddenHttpMethodFilter(): HiddenHttpMethodFilter {
        return object : HiddenHttpMethodFilter() {
            override fun doFilterInternal(
                request: HttpServletRequest,
                response: HttpServletResponse,
                filterChain: FilterChain
            ) {
                filterChain.doFilter(request, response)
            }
        }
    }

    @Bean
    fun customRouteLocator(builder: RouteLocatorBuilder, userIsMemberFilter: UserIsMemberFilter): RouteLocator {
        return builder.routes {

            // PROJECT SERVICE

            route {
                // getAllProjects, getProject, getAllProjectsOfUser
                path(
                    "/${ProjectEndpoint.BASE.url}",
                    "/${ProjectEndpoint.BASE.url}/*",
                    "/${ProjectEndpoint.BASE.url}/users/**"
                )
                predicate { exchange ->
                    logger.debug("http: ${exchange.request.method} | predicate get routes of projectService: ${exchange.request.method == HttpMethod.GET}")
                    exchange.request.method == HttpMethod.GET
                }
                uri(ProjectEndpoint.SERVICE.url)
            }

            route {
                // deleteProject, updateProject
                path("/${ProjectEndpoint.BASE.url}/{projectId}")
                predicate { exchange ->
                    logger.debug("http: ${exchange.request.method} | predicate delete and update routes of projectService: ${exchange.request.method == HttpMethod.PUT || exchange.request.method == HttpMethod.DELETE}")
                    exchange.request.method == HttpMethod.PUT || exchange.request.method == HttpMethod.DELETE
                }.filters { f ->
                    logger.debug("outer delete filter")
                    f.filter { exchange, chain ->
                        val modifiedRequest = exchange.request
                            .mutate().path("${exchange.request.path}/users/${user.id}").build()
                        chain.filter(exchange.mutate().request(modifiedRequest).build())
                    }
                }
                uri(ProjectEndpoint.SERVICE.url)
            }

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




            route {
                path(
                    "/${ProjectEndpoint.BASE.url}"
                )
                    .filters { f ->
                        f.modifyRequestBody(
                            GatewayProjectDTO::class.java, ProjectDTO::class.java,
                            MediaType.APPLICATION_JSON_VALUE
                        )
                        { _, projectDTO -> Mono.just(ProjectDTO(projectDTO.name, user.id, projectDTO.members)) }
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


