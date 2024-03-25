package org.hyperskill.community.flashcards.integration;

import ch.qos.logback.classic.Level;
import org.hyperskill.community.flashcards.category.CategoryController;
import org.hyperskill.community.flashcards.category.CategoryService;
import org.hyperskill.community.flashcards.category.model.Category;
import org.hyperskill.community.flashcards.category.model.CategoryAccess;
import org.hyperskill.community.flashcards.common.AuthenticationResolver;
import org.hyperskill.community.flashcards.common.RedirectController;
import org.hyperskill.community.flashcards.config.ObservabilityConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.oneOf;
import static org.hyperskill.community.flashcards.TestUtils.TEST1;
import static org.hyperskill.community.flashcards.TestUtils.TEST2;
import static org.hyperskill.community.flashcards.TestUtils.jwtUser;
import static org.hyperskill.community.flashcards.TestUtils.oidc;
import static org.hyperskill.community.flashcards.TestUtils.startLogAppender;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({RedirectController.class, CategoryController.class,
        AuthenticationResolver.class, ObservabilityConfiguration.class})
@TestPropertySource(properties = {"spring.profiles.active=test"})
class RedirectControllerIT {

    // needed since otherwise test tries to connect to Authorization server on Spring security context creation
    @MockBean
    ClientRegistrationRepository clientRegistrationRepository;

    @MockBean
    CategoryService categoryService;

    @Autowired
    MockMvc mockMvc;

    @ParameterizedTest
    @ValueSource(strings = {"/api/categories", "/categories/1", "/categories"})
    void whenEndpointCalledUnauthenticated_requestRedirectedToLogin(String url) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("http://*/login"));
    }

    @Test
    void whenExistingApiEndpointCalledAuthenticated_200ReturnedAndNoForward() throws Exception {
        when(categoryService.findById(TEST2, "id")).thenReturn(
                new Category("id", "test-cat", null, Set.of(new CategoryAccess(TEST2, "r")))
        );
        mockMvc.perform(get("/api/categories/id")
                        .with(oidc(TEST2)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("test-cat")))
                .andExpect(forwardedUrl(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/categories", "/categories/1", "/notExisting", "/notExisting/111/any"})
    void whenNonApiEndpointCalledAuthenticated_requestForwardedToBaseUrl(String url) throws Exception {
        mockMvc.perform(get(url)
                        .with(oidc(TEST2)))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/"));
    }

    @Test
    void whenForwarded_ObservabilityTraceLogged() throws Exception {
        final var logs = startLogAppender(RedirectController.class);

        mockMvc.perform(get("/categories")
                        .with(oidc(TEST2)))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/"));
        assertTrue(logs.stream().anyMatch(event -> event.getLevel() == Level.TRACE));
    }

    @Test
    void whenBaseUrlCalledAuthenticated_requestForwardedToVueApp() throws Exception {
        mockMvc.perform(get("/")
                        //test jwt for a change (used by http-client calls)
                        .with(jwt().jwt(jwtUser(TEST1))))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("index.html")); // index.html contains Vue app div
    }

    @ParameterizedTest
    @ValueSource(strings = {"/some.css", "/assets/some.js", "/favicon.ico"})
    void whenResourceEndpointCalledAuthenticated_NoForward(String assetUrl) throws Exception {
        mockMvc.perform(get(assetUrl)
                        .with(oidc(TEST2)))
                // favicon exists = 200, others = 404
                .andExpect(status().is(oneOf(200, 404)))
                .andExpect(forwardedUrl(null));
    }

    @Test
    void whenResourceServed_NoObservabilityTraceLogged() throws Exception {
        final var logs = startLogAppender(RedirectController.class);

        mockMvc.perform(get("/favicon.ico")
                        .with(oidc(TEST2)))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(null));
        assertTrue(logs.stream().noneMatch(event -> event.getLevel() == Level.TRACE));
    }

}
