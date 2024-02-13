package org.hyperskill.community.flashcards.common;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationResolver {

    private final Environment env;

    public String resolveUsername() {
        if (Boolean.TRUE.equals(env.getProperty("DEV_MODE", Boolean.class, false))) {
            return env.getProperty("DEV_USER", "test1@test.com");
        }
        var authentication = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return switch (authentication) {
            case OidcUser oidcUser -> oidcUser.getSubject();
            // to provide access for Client Credentials Jwt-token of http-client, http or Postman
            case Jwt jwt -> jwt.getSubject();
            default -> throw new IllegalStateException("Unexpected authentication: "
                                                       + authentication.getClass().getName());
        };
    }
}
