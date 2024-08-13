package org.hyperskill.community.flashcards.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyperskill.community.flashcards.TestMongoConfiguration;
import org.hyperskill.community.flashcards.category.response.CategoryPageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

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
class CategoryControllerIT {

    // needed since otherwise test tries to connect to Authorization server on AppContext creation
    @MockBean
    ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void getCategoriesUnauthenticated_Gives401() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getCategoriesValidationError_Gives400() throws Exception {
        mockMvc.perform(get("/api/categories?page=-1")
                        .with(oidc(TEST1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCategoriesNotOwner_givesEmptyResponse() throws Exception {
        mockMvc.perform(get("/api/categories")
                        .with(jwt().jwt(jwtUser(TEST2)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.isLast").value(true))
                .andExpect(jsonPath("$.categories").isEmpty());
    }

    @Test
    void getCategoriesOwnerDefaultPage0_givesExampleCategory() throws Exception {
        mockMvc.perform(get("/api/categories")
                        .with(oidc(TEST1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories").isArray())
                .andExpect(jsonPath("$.categories[0].name").value("example"));
    }

    @Test // unfortunately cannot be tested with @ParameterizedTest because of db.drop()
    void getCategoriesOwnerExplicitPage0_givesExampleCategory() throws Exception {
        mockMvc.perform(get("/api/categories?page=0")
                        .with(oidc(TEST1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories").isArray())
                .andExpect(jsonPath("$.categories[0].name").value("example"));
    }

    @Test
    void getCategoryById_WorksIfPermitted403IfNot() throws Exception {
        var category = mockMvc.perform(get("/api/categories?page=0")
                        .with(oidc(TEST1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories[0].name").value("example"))
                .andReturn();
        var response = objectMapper.readValue(category.getResponse().getContentAsString(), CategoryPageResponse.class);
        var exampleId = response.categories().getFirst().id();
        mockMvc.perform(get("/api/categories/" + exampleId)
                        .with(oidc(TEST1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("example"));
        mockMvc.perform(get("/api/categories/" + exampleId)
                        .with(oidc(TEST2)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getCategoriesOwnerPage1_isEmpty() throws Exception {
        mockMvc.perform(get("/api/categories?page=1")
                        .with(oidc(TEST1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories").isEmpty());
    }

    @Test
    void createCategory_creates() throws Exception {
        var result = mockMvc.perform(post("/api/categories")
                .with(oidc(TEST1)).with(csrf()).contentType("application/json")
                .content("{\"name\":\"test\"}"))
                .andExpect(status().isCreated())
                .andReturn();
        var id = Objects.requireNonNull(result.getResponse().getHeader("Location")).split("/")[3];
        mockMvc.perform(get("/api/categories/" + id)
                        .with(oidc(TEST1)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"));
    }

    @Test
    void updateCategory_updates() throws Exception {
        var result = mockMvc.perform(post("/api/categories")
                        .with(oidc(TEST1)).with(csrf()).contentType("application/json")
                        .content("{\"name\":\"to-update\"}"))
                .andExpect(status().isCreated())
                .andReturn();
        var id = Objects.requireNonNull(result.getResponse().getHeader("Location")).split("/")[3];
        mockMvc.perform(put("/api/categories/" + id)
                        .with(oidc(TEST1)).with(csrf()).contentType("application/json")
                .content("{\"name\":\"updated\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("updated"));
    }

    @Test
    void deleteCategory_deletes() throws Exception {
        var result = mockMvc.perform(post("/api/categories")
                        .with(oidc(TEST1)).with(csrf()).contentType("application/json")
                        .content("{\"name\":\"to-delete\"}"))
                .andExpect(status().isCreated())
                .andReturn();
        var id = Objects.requireNonNull(result.getResponse().getHeader("Location")).split("/")[3];
        mockMvc.perform(delete("/api/categories/" + id)
                        .with(oidc(TEST1)).with(csrf()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/categories/" + id)
                        .with(oidc(TEST1)).with(csrf()))
                .andExpect(status().isNotFound());
    }

}
