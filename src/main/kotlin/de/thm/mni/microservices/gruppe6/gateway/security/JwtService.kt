package de.thm.mni.microservices.gruppe6.gateway.security

import de.thm.mni.microservices.gruppe6.lib.classes.userService.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import java.security.Key
import java.util.*


@Component
class JwtService(private val jwtProperties: JWTProperties) {

    private val key: Key = Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun authorize(serverWebExchange: ServerWebExchange): User {
        return authorize(serverWebExchange.request.headers[HttpHeaders.AUTHORIZATION]!![0])
    }

    fun authorize(jwt: String): User {
        val claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .requireSubject(jwtProperties.jwtSubject)
                    .build()
                    .parseClaimsJws(jwt)
                    .body
        return User(claims)
    }

    fun createToken(user: User): String {
        return Jwts.builder()
            .setSubject(jwtProperties.jwtSubject)
            .addClaims(user.getJwtClaims())
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + (1000 * jwtProperties.expiration)))
            .signWith(key)
            .compact()
    }

}
