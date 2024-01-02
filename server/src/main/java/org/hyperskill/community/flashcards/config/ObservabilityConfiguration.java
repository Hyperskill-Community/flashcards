package org.hyperskill.community.flashcards.config;

import jakarta.servlet.Filter;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class ObservabilityConfiguration {

    @Bean
    Filter correlationFilter() {
        return (request, response, chain) -> {
            var loggedIn = SecurityContextHolder.getContext().getAuthentication();
            MDC.put("user", loggedIn.getName());
            chain.doFilter(request, response);
        };
    }
}
