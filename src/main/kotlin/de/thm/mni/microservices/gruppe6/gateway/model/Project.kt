package de.thm.mni.microservices.gruppe6.gateway.model

import java.time.LocalDateTime
import java.util.*

data class Project(
        var id: UUID?,
        var name: String,
        var creatorId: UUID?,
        var createTime: LocalDateTime,
)