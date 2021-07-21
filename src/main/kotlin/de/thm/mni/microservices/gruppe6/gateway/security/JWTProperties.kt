package de.thm.mni.microservices.gruppe6.gateway.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "jwt")
@ConstructorBinding
data class JWTProperties(

    /**
     * Secret for JWT signature
     */
    val secret: String,

    /**
     * validity duration of JWT in seconds
     */
    val expiration: Int,

    val jwtSubject: String = "service-auth"

)
