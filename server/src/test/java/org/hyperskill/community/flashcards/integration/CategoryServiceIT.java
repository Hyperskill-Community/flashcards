package org.hyperskill.community.flashcards.integration;

import org.hyperskill.community.flashcards.TestMongoConfiguration;
import org.hyperskill.community.flashcards.category.CategoryService;
import org.hyperskill.community.flashcards.category.model.Category;
import org.hyperskill.community.flashcards.category.model.CategoryAccess;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
@ContextConfiguration(classes = TestMongoConfiguration.class)
class CategoryServiceIT {

    // needed since otherwise test tries to connect to Authorization server on AppContext creation
    @MockBean
    ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    CategoryService service;

    HashMap<String, String> idMaps = new HashMap<>();

    @BeforeEach
    void setup() {
        service = new CategoryService(mongoTemplate);
        setupCategories();
    }

    @AfterEach
    void teardown() {
        mongoTemplate.getDb().drop();
    }

    @Test
    void whenUser1getsCategories_allReturned() {
        var page = service.getCategories("user1", 0);
        assertEquals(3, page.getTotalElements());
        assertEquals(1, page.getTotalPages());
        assertEquals("cat1", page.getContent().getFirst().name());
        assertEquals("cat4", page.getContent().getLast().name());
    }

    @Test
    void whenUser1findCat2ById_ReturnedCorrect() {
        var id = idMaps.get("cat2");
        var category = service.findById("user1", id);
        assertEquals("cat2", category.name());
    }

    @Test
    void whenUser1findCat4ByIdDelete_AccessDenied() {
        var id = idMaps.get("cat2");
        assertThrows(AccessDeniedException.class, () -> service.findById("user1", id, "d"));
    }

    @Test
    void whenUser1findCat3ById_AccessDenied() {
        var id = idMaps.get("cat3");
        assertThrows(AccessDeniedException.class, () -> service.findById("user1", id));
    }

    private void setupCategories() {
        mongoTemplate.getCollection("category");
        addCategory("cat2", "user1", "r", "user2", "rw");
        addCategory("cat4", "user1", "rw", "user2", "r");
        addCategory("cat1", "user1", "rwd");
        addCategory("cat3", "user2", "rwd");
    }

    private void addCategory(String name, String...access) {
        var categoryAccess = new HashSet<CategoryAccess>();
        for (var i = 0; i < access.length; i += 2) {
            categoryAccess.add(new CategoryAccess(access[i], access[i + 1]));
        }
        var saved = mongoTemplate.save(new Category(null, name, categoryAccess), "category");
        idMaps.put(name, saved.id());
    }

}
