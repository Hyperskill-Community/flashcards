package org.hyperskill.community.flashcards.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyperskill.community.flashcards.TestMongoConfiguration;
import org.hyperskill.community.flashcards.registration.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestMongoConfiguration.class)
@AutoConfigureMockMvc
class RegistrationIT {

    // needed since otherwise test tries to connect to Authorization server on AppContext creation
    @MockBean
    ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    UserDetailsService userDetailsService;

    @Test
    void registerUnauthenticatedValidJson_AddsUser() throws Exception {
        String username = "hans.wurst@xyz.de";
        // user not existing
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new UserDto(username, "12345678"))))
                .andExpect(status().isOk());
        // user exists now
        assertEquals(username, userDetailsService.loadUserByUsername(username).getUsername());
    }

    @Test
    void registerUnauthenticatedExistingUser_Gives400() throws Exception {
        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new UserDto("test@xyz.de", "12345678"))))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/register") // and again
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new UserDto("test@xyz.de", "12345678"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUnauthenticatedInvalidDto_Gives400() throws Exception {
        mockMvc.perform(post("/api/register") // and again
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new UserDto("wrong", "1234"))))
                .andExpect(status().isBadRequest());
    }
}
