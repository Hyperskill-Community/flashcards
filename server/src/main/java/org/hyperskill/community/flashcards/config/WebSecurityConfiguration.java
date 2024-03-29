package org.hyperskill.community.flashcards.config;

import lombok.extern.slf4j.Slf4j;
import org.hyperskill.community.flashcards.registration.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Optional;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Spring security 6.x style provision of SecurityFilterChain bean with the security configuration,
 * as well as PasswordProvider and AuthenticationManager that makes use of our UserDetails persistence.
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfiguration {

    @Bean
    @ConditionalOnProperty(name = "DEV_MODE", havingValue = "false", matchIfMissing = true)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(CsrfConfigurer::disable)
                .oauth2ResourceServer(auth -> auth.jwt(withDefaults()))
                .oauth2Login(withDefaults())
                .oauth2Client(withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register.html", "/js/register.js", "/css/register.css").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
                        .anyRequest().authenticated()
                ).build();
    }

    @Bean
    public UserDetailsService userDetailsService(MongoTemplate mongoTemplate) {
        return username ->
                Optional.ofNullable(mongoTemplate.findById(username, User.class))
                        .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnProperty(name = "DEV_MODE", havingValue = "true")
    public SecurityFilterChain filterChainDevMode(HttpSecurity http) throws Exception {
        log.warn("Running in DEV_MODE, permitting all requests.");
        return http
                .csrf(CsrfConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests(
                        auth -> auth.anyRequest().permitAll()
                ).build();
    }

    @Bean
    @ConditionalOnProperty(name = "DEV_MODE", havingValue = "true")
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("Configuring CORS for DEV MODE");
        var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization",
                "Access-Control-Allow-Origin", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization",
                "Access-Control-Allow-Origin", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
