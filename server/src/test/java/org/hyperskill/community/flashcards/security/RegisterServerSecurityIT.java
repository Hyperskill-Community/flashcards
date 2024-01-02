package org.hyperskill.community.flashcards.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyperskill.community.flashcards.registration.UserDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Disabled // must fix test container connections...
@DisabledInAotMode // bug in Spring 3.2 @MockBean does not work in AOT mode (https://github.com/spring-projects/spring-boot/issues/36997)
class RegisterServerSecurityIT {

    @Autowired
    MockMvc mockMvc;

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDbContainer =  new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        mongoDbContainer.start();
    }

    @AfterAll
    static void tearDown() {
        mongoDbContainer.stop();
    }

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
