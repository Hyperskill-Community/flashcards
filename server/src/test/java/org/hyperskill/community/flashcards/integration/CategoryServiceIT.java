package org.hyperskill.community.flashcards.integration;

import org.hyperskill.community.flashcards.TestMongoConfiguration;
import org.hyperskill.community.flashcards.category.CategoryService;
import org.hyperskill.community.flashcards.category.model.Category;
import org.hyperskill.community.flashcards.category.model.CategoryAccess;
import org.hyperskill.community.flashcards.category.request.CategoryCreateRequest;
import org.hyperskill.community.flashcards.category.request.CategoryUpdateRequest;
import org.hyperskill.community.flashcards.common.exception.ResourceAlreadyExistsException;
import org.hyperskill.community.flashcards.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@ContextConfiguration(classes = TestMongoConfiguration.class)
class CategoryServiceIT {

    @Autowired
    MongoTemplate mongoTemplate;

    CategoryService service;

    HashMap<String, String> idMaps = new HashMap<>();

    @BeforeEach
    void setup() {
        mongoTemplate.getDb().drop();
        service = new CategoryService(mongoTemplate);
        setupCategories();
    }

    @Test
    void whenUserGetsCategories_rightOnesReturned() {
        var page = service.getCategories("user1", 0);
        assertEquals(3, page.getTotalElements());
        assertEquals(1, page.getTotalPages());
        assertEquals("cat1", page.getContent().getFirst().name());
        assertEquals("cat4", page.getContent().getLast().name());
    }

    @Test
    void whenUserIsOwnerAndHasReadRight_findReturnsCategory() {
        var id = idMaps.get("cat2");
        var category = service.findById("user1", id);
        assertEquals("cat2", category.name());
    }

    @Test
    void whenUserIsNotOwner_FindThrowsAccessDenied() {
        var id = idMaps.get("cat3");
        assertThrows(AccessDeniedException.class, () -> service.findById("user1", id));
    }

    @Test
    void whenUserIsOwnerButNoPermission_FindThrowsAccessDenied() {
        var id = idMaps.get("cat2");
        assertThrows(AccessDeniedException.class, () -> service.findById("user1", id, "d"));
    }

    @Test
    void whenCategoryNotExists_AllMethodsThrowAccessDenied() {
        assertThrows(ResourceNotFoundException.class,
                () -> service.findById("user1", "no-existing-id"));
        var request = new CategoryUpdateRequest("new", null);
        assertThrows(ResourceNotFoundException.class,
                () -> service.updateById("user1", "no-existing-id", request));
        assertThrows(ResourceNotFoundException.class,
                () -> service.deleteById("user1", "no-existing-id"));
    }

    @Test
    void whenCategoryExists_CreateAndRenameThrow() {
        var id = idMaps.get("cat1");
        var update = new CategoryUpdateRequest("cat3", "some");
        assertThrows(ResourceAlreadyExistsException.class,
                () -> service.updateById("user1", id, update));
        var create = new CategoryCreateRequest("cat1", "description");
        assertThrows(ResourceAlreadyExistsException.class,
                () -> service.createCategory("user1", create));
    }

    @Test
    void whenPermissionOk_deleteDeletes() {
        var id = idMaps.get("cat1");
        service.deleteById("user1", id);
        var page = service.getCategories("user1", 0);
        assertEquals(2, page.getTotalElements());
        assertEquals("cat2", page.getContent().getFirst().name());
        assertFalse(mongoTemplate.getCollectionNames().contains("cat1"));
    }

    @Test
    void whenNoDeletePermission_deleteThrowsAccessDenied() {
        var id = idMaps.get("cat2");
        assertThrows(AccessDeniedException.class, () -> service.deleteById("user1", id));
    }

    @Test
    void whenPermissionOk_updateUpdates() {
        var id = idMaps.get("cat1");
        service.updateById("user1", id, new CategoryUpdateRequest("new", "brandnew"));
        var page = service.getCategories("user1", 0);
        assertEquals(3, page.getTotalElements());
        assertEquals("cat2", page.getContent().getFirst().name());
        assertEquals("new", page.getContent().getLast().name());
        assertEquals("brandnew", page.getContent().getLast().description());
        assertFalse(mongoTemplate.getCollectionNames().contains("cat1"));
        assertTrue(mongoTemplate.getCollectionNames().contains("new"));
    }

    @Test
    void whenNoWritePermission_updateThrowsAccessDenied() {
        var id = idMaps.get("cat2");
        var update = new CategoryUpdateRequest("new", null);
        assertThrows(AccessDeniedException.class, () -> service.updateById("user1", id, update));
    }

    @Test
    void whenNoCollisions_createCreates() {
        var newId = service.createCategory("user1", new CategoryCreateRequest("new", "descr"));
        var page = service.getCategories("user1", 0);
        assertEquals(4, page.getTotalElements());
        assertEquals("new", page.getContent().getLast().name());
        var expectedCategory = new Category(newId, "new", "descr",
                Set.of(new CategoryAccess("user1", "rwd")));
        assertEquals(expectedCategory, mongoTemplate.findById(newId, Category.class));
    }

    @Test
    void whenManyCategories_pagingWorks() {
        var access = Set.of(new CategoryAccess("user1", "rwd"));
        for (int i = 0; i < 20; i++) {
            mongoTemplate.save(new Category(null, "new%d".formatted(i), null, access), "category");

        }
        // 20 new and 3 previous - visible for user1
        assertEquals(23, service.getCategories("user1", 0).getTotalElements());
        assertEquals(20, service.getCategories("user1", 0).getNumberOfElements());
        assertEquals(2, service.getCategories("user1", 0).getTotalPages());
        assertEquals(3, service.getCategories("user1", 1).getNumberOfElements());
        assertTrue(service.getCategories("user1", 2).isEmpty());
    }

    private void setupCategories() {
        mongoTemplate.getCollection("category");
        addCategory("cat2", "user1", "r", "user2", "w");
        addCategory("cat4", "user1", "rw", "user2", "r");
        addCategory("cat1", "user1", "rwd");
        addCategory("cat3", "user2", "rwd");
    }

    private void addCategory(String name, String... access) {
        var categoryAccess = new HashSet<CategoryAccess>();
        for (var i = 0; i < access.length; i += 2) {
            categoryAccess.add(new CategoryAccess(access[i], access[i + 1]));
        }
        var saved = mongoTemplate.save(new Category(null, name, "descr",  categoryAccess), "category");
        idMaps.put(name, saved.id());
        mongoTemplate.getDb().createCollection(name);
    }

}
