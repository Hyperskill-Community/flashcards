package org.hyperskill.community.flashcards.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Set;
import java.util.stream.Stream;

import static org.hyperskill.community.flashcards.TestUtils.TEST1;
import static org.hyperskill.community.flashcards.TestUtils.TEST2;
import static org.hyperskill.community.flashcards.TestUtils.jwtUser;
import static org.hyperskill.community.flashcards.TestUtils.oidcUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@MockitoSettings
class AuthenticationResolverTest {

    AuthenticationResolver resolver;

    @Mock
    Environment env;

    @BeforeEach
    void setup() {
        resolver = new AuthenticationResolver(env);
    }

    static Stream<Arguments> whenJwtOrOidcAuthentication_resolverWorks() {
        return Stream.of(
                Arguments.of(new JwtAuthenticationToken(jwtUser(TEST2))),
                Arguments.of(new OAuth2AuthenticationToken(oidcUser(TEST2), Set.of(), "test"))
        );
    }

    @ParameterizedTest
    @MethodSource
    void whenJwtOrOidcAuthentication_resolverWorks(Authentication authentication) {
        var securityContext = new SecurityContextImpl(authentication);
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            assertEquals(TEST2, resolver.resolveUsername());
        }
    }

    @Test
    void whenOtherAuthentication_resolverThrows() {
        var securityContext = new SecurityContextImpl(new UsernamePasswordAuthenticationToken("user", "password"));
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            assertThrows(IllegalStateException.class, () -> resolver.resolveUsername());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "dev user"})
    void whenDevMode_resolverReturnsDevUser(String devUser) {
        when(env.getProperty("DEV_MODE", Boolean.class, false)).thenReturn(true);
        var expected = devUser.isBlank() ? TEST1 : devUser;
        when(env.getProperty("DEV_USER", TEST1)).thenReturn(expected);
        assertEquals(expected, resolver.resolveUsername());
    }

}
