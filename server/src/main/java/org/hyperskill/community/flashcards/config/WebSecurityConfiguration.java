package org.hyperskill.community.flashcards.config;

import org.hyperskill.community.flashcards.registration.User;
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

import java.util.Optional;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * new Spring security 6.0 style provision of SecurityFilterChain bean with the security configuration,
 * as well as PasswordProvider and AuthenticationManager that makes use of our UserDetails persistence.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(CsrfConfigurer::disable)
                .oauth2ResourceServer(auth -> auth.jwt(withDefaults()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register.html", "/js/register.js", "/css/register.css").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(withDefaults())
                .oauth2Client(withDefaults())
                .build();
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
}
