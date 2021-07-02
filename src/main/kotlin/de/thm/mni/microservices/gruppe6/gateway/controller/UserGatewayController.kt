package de.thm.mni.microservices.gruppe6.gateway.controller

import de.thm.mni.microservices.gruppe6.gateway.endpoints.UserEndpoint
import de.thm.mni.microservices.gruppe6.gateway.model.*
import de.thm.mni.microservices.gruppe6.gateway.service.GatewayService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/gateway/user")
@CrossOrigin
class UserGatewayController(@Autowired val gatewayService: GatewayService) {

    private val userId = "8d8fa2d7-b999-4e07-9739-c563ee9fb12b"

    @GetMapping("")
    fun getAllUsers(): Flux<User> =
            gatewayService.forwardGetRequestFlux(UserEndpoint.SERVICE.url,
                    "${UserEndpoint.BASE}",
                    User::class.java)

    @GetMapping("{userId}")
    fun getUserByID(@PathVariable userId: UUID): Mono<User> =
            gatewayService.forwardGetRequestMono(UserEndpoint.SERVICE.url,
                    "${UserEndpoint.BASE}/$userId",
                    User::class.java)

}