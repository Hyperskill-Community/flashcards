package org.hyperskill.community.flashcards.config;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@Slf4j
public class ObservabilityConfiguration {

    @Bean
    Filter correlationFilter() {
        return (request, response, chain) -> {
            var loggedIn = SecurityContextHolder.getContext().getAuthentication();
            MDC.put("user", loggedIn.getName());
            log.debug("User {} is accessing {}", loggedIn.getName(), ((HttpServletRequest) request).getRequestURI());
            chain.doFilter(request, response);
        };
    }
}
