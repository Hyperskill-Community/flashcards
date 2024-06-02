package org.hyperskill.community.flashcards.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyperskill.community.flashcards.TestMongoConfiguration;
import org.hyperskill.community.flashcards.card.request.QuestionAndAnswerRequestDto;
import org.hyperskill.community.flashcards.card.request.SingleChoiceQuizRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.hyperskill.community.flashcards.TestUtils.TEST1;
import static org.hyperskill.community.flashcards.TestUtils.TEST2;
import static org.hyperskill.community.flashcards.TestUtils.jwtUser;
import static org.hyperskill.community.flashcards.TestUtils.oidc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestMongoConfiguration.class)
@AutoConfigureMockMvc
class CardControllerIT {

    // needed since otherwise test tries to connect to Authorization server on AppContext creation
    @MockBean
    ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MongoTemplate mongoTemplate;

    String exampleCategoryId;

    @BeforeEach
    void setUp() {
        exampleCategoryId = Objects.requireNonNull(mongoTemplate.getCollection("category").find().first())
                .get("_id").toString();
    }

    @Test
    void getCardsUnauthenticated_Gives401() throws Exception {
        mockMvc.perform(get("/api/cards"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/cards?page=0",
            "/api/cards?categoryId=acbd&page=-1",
            "/api/cards",
            "/api/cards/id",
            "/api/cards",
            "/api/cards/count"})
    void getCardsValidationError_Gives400(String url) throws Exception {
        mockMvc.perform(get(url)
                        .with(oidc(TEST1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putpostDeleteCardsMissingCategoryId_Gives400() throws Exception {
        mockMvc.perform(put("/api/cards/id")
                        .with(oidc(TEST1))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
        mockMvc.perform(delete("/api/cards/id")
                        .with(oidc(TEST1))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/api/cards")
                        .with(oidc(TEST1))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }


    @ParameterizedTest
    @ValueSource(strings = {"/api/cards", "/api/cards/details"})
    void getCardsWithReadRights_givesInitialCardData(String endpoint) throws Exception {
        mockMvc.perform(get(endpoint + "?categoryId=" + exampleCategoryId)
                        .with(jwt().jwt(jwtUser(TEST1)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.isLast").value(false))
                .andExpect(jsonPath("$.cards[19]").exists())
                .andExpect(jsonPath("$.cards[20]").doesNotExist());
        mockMvc.perform(get(endpoint + "?page=2&categoryId=" + exampleCategoryId)
                        .with(jwt().jwt(jwtUser(TEST1)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isLast").value(true))
                .andExpect(jsonPath("$.cards[19]").exists())
                .andExpect(jsonPath("$.cards[20]").doesNotExist());
    }

    @Test
    void getCardsNoReadRights_gives403() throws Exception {
        mockMvc.perform(get("/api/cards?categoryId=" + exampleCategoryId)
                        .with(jwt().jwt(jwtUser(TEST2)))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void getCardsCountWithRights_givesAll() throws Exception {
        mockMvc.perform(get("/api/cards/count?categoryId=" + exampleCategoryId)
                        .with(jwt().jwt(jwtUser(TEST1)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(60));
    }

    @Test
    void createCards_AddsAndDeleteCardDeletes() throws Exception {
        var cardRequest = QuestionAndAnswerRequestDto.builder()
                .question("q")
                .answer("a")
                .tags(Set.of("t1", "t2"))
                .title("title")
                .build();
        var uri = mockMvc.perform(post("/api/cards?categoryId=" + exampleCategoryId)
                        .with(oidc(TEST1)).with(csrf()).contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(cardRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        mockMvc.perform(get(uri + "?categoryId=" + exampleCategoryId)
                        .with(jwt().jwt(jwtUser(TEST1)))
                        .with(csrf()))
                .andExpect(status().isOk());
        mockMvc.perform(delete(uri + "?categoryId=" + exampleCategoryId)
                        .with(jwt().jwt(jwtUser(TEST1)))
                        .with(csrf()))
                .andExpect(status().isOk());
        mockMvc.perform(get(uri + "?categoryId=" + exampleCategoryId)
                        .with(jwt().jwt(jwtUser(TEST1)))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCardOtherCardType_Updates() throws Exception {
        var cardRequest = SingleChoiceQuizRequestDto.builder()
                .question("q")
                .options(List.of("a", "b"))
                .correctOption(0)
                .tags(Set.of("t1", "t2"))
                .title("updated")
                .build();
        var uri = mockMvc.perform(post("/api/cards?categoryId=" + exampleCategoryId)
                        .with(oidc(TEST1)).with(csrf()).contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(cardRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        cardRequest = SingleChoiceQuizRequestDto.builder()
                .question("q")
                .options(List.of("a", "b"))
                .correctOption(1)
                .tags(Set.of("t1", "t2"))
                .title("updated")
                .build();
        mockMvc.perform(put(uri + "?categoryId=" + exampleCategoryId)
                        .with(oidc(TEST1)).with(csrf()).contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(cardRequest)))
                .andExpect(status().isOk());
        mockMvc.perform(get(uri + "?categoryId=" + exampleCategoryId)
                        .with(jwt().jwt(jwtUser(TEST1)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("updated"))
                .andExpect(jsonPath("$.correctOption").value(1));
        // clean up to not affect other tests
        mockMvc.perform(delete(uri + "?categoryId=" + exampleCategoryId)
                        .with(jwt().jwt(jwtUser(TEST1)))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void getCardsById_givesAll() throws Exception {
        mockMvc.perform(get("/api/cards/count?categoryId=" + exampleCategoryId)
                        .with(jwt().jwt(jwtUser(TEST1)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(60));
    }

}
