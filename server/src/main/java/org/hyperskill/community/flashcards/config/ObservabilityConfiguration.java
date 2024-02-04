package org.hyperskill.community.flashcards.config;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

@Configuration
@Slf4j
public class ObservabilityConfiguration {

    @Bean
    Filter correlationFilter() {
        return (request, response, chain) -> {
            var loggedIn = SecurityContextHolder.getContext().getAuthentication();
            MDC.put("user", loggedIn.getName());
            var uri = ((HttpServletRequest) request).getRequestURI();
            var queryString =  ((HttpServletRequest) request).getQueryString();

            if (!uri.contains(".")) {
                log.trace("Accessing {}{}", uri, Objects.isNull(queryString) ? "" : "?" + queryString);
            }
            chain.doFilter(request, response);
        };
    }
}
