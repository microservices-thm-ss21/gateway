package de.thm.mni.microservices.gruppe6.gateway.controller

import de.thm.mni.microservices.gruppe6.gateway.endpoints.IssueEndpoint
import de.thm.mni.microservices.gruppe6.gateway.model.*
import de.thm.mni.microservices.gruppe6.gateway.service.GatewayService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/gateway/issues")
@CrossOrigin
class IssueGatewayController(@Autowired val gatewayService: GatewayService) {

    private val userId = "8d8fa2d7-b999-4e07-9739-c563ee9fb12b"

    @GetMapping("")
    fun getAllIssues(): Flux<Issue> =
            gatewayService.forwardGetRequestFlux(IssueEndpoint.SERVICE.url,
                    IssueEndpoint.BASE.url,
                    Issue::class.java)

    @GetMapping("/{issueId}")
    fun getIssue(@PathVariable issueId: UUID): Mono<Issue> =
            gatewayService.forwardGetRequestMono(IssueEndpoint.SERVICE.url,
                    "${IssueEndpoint.BASE.url}/$issueId",
                    Issue::class.java)

    @GetMapping("/project/{projectId}")
    fun getAllProjectIssues(@PathVariable projectId: UUID): Flux<Issue> =
            gatewayService.forwardGetRequestFlux(IssueEndpoint.SERVICE.url,
                    "${IssueEndpoint.BASE.url}/$projectId",
                    Issue::class.java)
}