package de.thm.mni.microservices.gruppe6.gateway.endpoints

enum class ProjectEndpoint(val url: String) {
    SERVICE("http://project-service:8082"),
    BASE("api/projects")
}
