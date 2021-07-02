package de.thm.mni.microservices.gruppe6.gateway.endpoints

enum class IssueEndpoint(val url: String) {
    SERVICE("http://issue-service:8081"),
    BASE("api/issues")
}