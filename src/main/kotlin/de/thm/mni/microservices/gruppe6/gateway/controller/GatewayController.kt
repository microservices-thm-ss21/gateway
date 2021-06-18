package de.thm.mni.microservices.gruppe6.gateway.controller

import de.thm.mni.microservices.gruppe6.gateway.model.Project
import de.thm.mni.microservices.gruppe6.gateway.service.GatewayService
import de.thm.mni.microservices.gruppe6.lib.exception.ServiceException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import java.util.*

@Controller
@RequestMapping("myDirtyProject")
@CrossOrigin
class GatewayController(@Autowired val gatewayService: GatewayService) {


    @GetMapping("projects")
    fun getAvailableProjects(): Flux<Project> {
        //val userId: UUID = gatewayService.validateUserToken("TODO")

        val client = WebClient.create("http://http:project-service:8082")
        val uriSpec: WebClient.UriSpec<WebClient.RequestBodySpec> = client.post()
        val headerSpec: WebClient.RequestHeadersSpec<*> = uriSpec.uri("api/projects/") //uriSpec.uri("api/projects/user/$userId")
        val responseSpec = headerSpec.header(
            HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE
        )
            .accept(MediaType.APPLICATION_JSON)
            .acceptCharset(StandardCharsets.UTF_8)

        return responseSpec.exchangeToFlux { response: ClientResponse ->
            if (response.statusCode() == HttpStatus.OK) {
                response.bodyToFlux(Project::class.java)
            } else {
                Flux.empty()
            }
        }
    }
/*
    /**
     * Returns all stored projects
     */
    @GetMapping("")
    fun getAllProjects(): Flux<Project> = projectService.getAllProjects()

    /**
     * Returns project with given id
     * @param id: project id
     */
    @GetMapping("{id}")
    fun getProject(@PathVariable id: UUID): Mono<Project> = projectService.getProjectById(id).switchIfEmpty { Mono.error(ServiceException(HttpStatus.NOT_FOUND)) }

    /**
     * Creates a new project with members
     */
    @PostMapping("")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun createProject(@RequestBody projectDTO: ProjectDTO): Mono<Project> {
        val project = projectService.createProjectWithMembers(projectDTO)
        return project.onErrorResume { Mono.error(ServiceException(HttpStatus.CONFLICT, it)) }
    }

    /**
     * Updates project details with given id
     * @param id: project id
     */
    @PutMapping("{id}")
    fun updateProject(@PathVariable id: UUID, @RequestBody projectDTO: ProjectDTO): Mono<Project> = projectService.updateProject(id, projectDTO).onErrorResume { Mono.error(ServiceException(HttpStatus.CONFLICT, "Either Project creator or Member(s) does not exist", it)) }

    /**
     * Deletes project with given id
     * @param id: project id
     */
    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun deleteProject(@PathVariable id: UUID): Mono<Void> = projectService.deleteProject(id)

/*
    @GetMapping("")
    fun getAllIssues(): Flux<Issue> = issueService.getAllIssues()

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun createIssue(@RequestBody issueDTO: IssueDTO): Mono<Issue> =
        issueService.createIssue(issueDTO).onErrorResume { Mono.error(ServiceException(HttpStatus.CONFLICT, it)) }

    @GetMapping("{issueId}")
    fun getIssue(@PathVariable issueId: UUID): Mono<Issue> =
        issueService.getIssue(issueId).switchIfEmpty(Mono.error(ServiceException(HttpStatus.NOT_FOUND)))

    @PutMapping("{issueId}")
    fun updateIssue(
        @PathVariable issueId: UUID,
        @RequestBody issueDTO: IssueDTO
    ): Mono<Issue> = issueService.updateIssue(issueId, issueDTO).onErrorResume { Mono.error(ServiceException(HttpStatus.CONFLICT, it)) }

    @DeleteMapping("{issueId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteIssue(@PathVariable issueId: UUID): Mono<Void> =
        issueService.deleteIssue(issueId)

    @GetMapping("project/{projectId}")
    fun getAllProjectIssues(@PathVariable projectId: UUID): Flux<Issue> = issueService.getAllProjectIssues(projectId)
*/
*/

}
