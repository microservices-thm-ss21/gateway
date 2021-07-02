package de.thm.mni.microservices.gruppe6.gateway.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class User(
        var id: UUID? = null,
        var username: String,
        var name: String,
        var lastName: String,
        var email: String,
        var dateOfBirth: LocalDate,
        var createTime: LocalDateTime,
        var globalRole: String,
        var lastLogin: LocalDateTime?
)
