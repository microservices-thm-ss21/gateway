package de.thm.mni.microservices.gruppe6.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.filter.HiddenHttpMethodFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class, R2dbcAutoConfiguration::class])
@ConfigurationPropertiesScan("de.thm.mni.microservices.gruppe6.gateway.security")
class GatewayApplication {


    /**
     * Override doFilterInternal to make HTTP-Methods delete and put work
     * Explanation: https://www.programmersought.com/article/38211750966/
     */
    @Bean
    fun hiddenHttpMethodFilter(): HiddenHttpMethodFilter {
        return object : HiddenHttpMethodFilter() {
            override fun doFilterInternal(
                request: HttpServletRequest,
                response: HttpServletResponse,
                filterChain: FilterChain
            ) {
                filterChain.doFilter(request, response)
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args)
}


