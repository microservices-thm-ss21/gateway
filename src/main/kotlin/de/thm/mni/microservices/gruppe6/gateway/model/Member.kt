package de.thm.mni.microservices.gruppe6.gateway.model

import java.util.*

data class Member(
        var id: UUID?,
        var projectId: UUID,
        var userId: UUID,
        var projectRole: String
)
