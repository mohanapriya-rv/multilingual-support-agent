package com.support.agent.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {

    @Bean
    fun corsFilter(): CorsFilter {
        val config = CorsConfiguration().apply {
            allowCredentials = true
            addAllowedOrigin("https://multilingual-support-agent-1.onrender.com")
            addAllowedOrigin("https://multilingual-support-agent.onrender.com")
            addAllowedOrigin("http://localhost:3000")
            addAllowedOrigin("http://localhost:3001")
            addAllowedHeader("*")
            addAllowedMethod("*")
            addExposedHeader("*")
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)

        return CorsFilter(source)
    }
}
