package org.hyperskill.community.authserver.config;

import org.hyperskill.community.authserver.userdetails.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

/**
 * new Spring security 6.0 style provision of SecurityFilterChain bean with the security configuration,
 * as well as PasswordProvider and AuthenticationManager that makes use of our UserDetails persistence.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

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
