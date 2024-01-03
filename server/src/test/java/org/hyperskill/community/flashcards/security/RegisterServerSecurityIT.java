package org.hyperskill.community.flashcards.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyperskill.community.flashcards.registration.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class RegisterServerSecurityIT {

    @Autowired
    MockMvc mockMvc;

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDbContainer;

    static {
        mongoDbContainer = new MongoDBContainer("mongo:latest");
        mongoDbContainer.setPortBindings(List.of("27017:27017"));
    }

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void registerUnauthenticatedValidJson_AddsUser() throws Exception {
        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new UserDto("hans.wurst@xyz.de", "12345678"))))
                .andExpect(status().isOk());
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
