package de.thm.mni.microservices.gruppe6.gateway.endpoints

enum class NewsEndpoint(val url: String) {
    SERVICE("http://news-service:8084"),
    BASE("api/news"),
}