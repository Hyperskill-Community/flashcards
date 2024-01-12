package org.hyperskill.community.flashcards.common;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationResolver {

    public String resolveUsername() {
        var authentication = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return switch (authentication) {
            case OidcUser oidcUser -> oidcUser.getSubject();
            // to provide access for Client Credentials Jwt-token of http-client, http or Postman
            case Jwt jwt -> "test1@test.com"; // better use from category example (or json)
            default -> throw new IllegalStateException("Unexpected authentication: "
                                                       + authentication.getClass().getName());
        };
    }
}
