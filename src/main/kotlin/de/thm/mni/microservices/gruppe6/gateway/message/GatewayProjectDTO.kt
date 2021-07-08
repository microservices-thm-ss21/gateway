package de.thm.mni.microservices.gruppe6.gateway.message

import de.thm.mni.microservices.gruppe6.lib.classes.projectService.MemberDTO

data class GatewayProjectDTO(
        var name: String,
        var members: List<MemberDTO>
)
