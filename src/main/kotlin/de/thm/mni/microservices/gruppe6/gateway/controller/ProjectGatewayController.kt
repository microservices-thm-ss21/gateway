package de.thm.mni.microservices.gruppe6.gateway.controller

import de.thm.mni.microservices.gruppe6.gateway.endpoints.ProjectEndpoint
import de.thm.mni.microservices.gruppe6.gateway.model.*
import de.thm.mni.microservices.gruppe6.gateway.service.GatewayService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/gateway/projects")
@CrossOrigin
class ProjectGatewayController(@Autowired val gatewayService: GatewayService) {

    private val userId = "8d8fa2d7-b999-4e07-9739-c563ee9fb12b"

    @GetMapping("/user")
    fun getAvailableProjects(): Flux<Project> =
            gatewayService.forwardGetRequestFlux(ProjectEndpoint.SERVICE.url,
                    "${ProjectEndpoint.BASE.url}/${ProjectEndpoint.PROJECTS_BY_USER.url}/$userId",
                    Project::class.java)

    @GetMapping("/user/project/{projectId}")
    fun getProjectByID(@PathVariable projectId: UUID): Mono<Project> =
            gatewayService.forwardGetRequestMono(ProjectEndpoint.SERVICE.url,
                    "${ProjectEndpoint.BASE.url}/$projectId",
                    Project::class.java)

    @GetMapping("/project/{projectId}/member")
    fun getMembersOfProject(@PathVariable projectId: UUID): Flux<Member> =
            gatewayService.forwardGetRequestFlux(ProjectEndpoint.SERVICE.url,
                    "${ProjectEndpoint.BASE.url}/$projectId/members",
                    Member::class.java)

}