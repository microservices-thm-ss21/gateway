package de.thm.mni.microservices.gruppe6.gateway.endpoints

enum class UserEndpoint(val url: String) {
    SERVICE("http://user-service:8083"),
    BASE("api/users"),
}