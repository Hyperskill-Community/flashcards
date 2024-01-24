package org.hyperskill.community.flashcards.category;

import org.hyperskill.community.flashcards.category.mapper.CategoryMapper;
import org.hyperskill.community.flashcards.category.model.Category;
import org.hyperskill.community.flashcards.category.model.CategoryAccess;
import org.hyperskill.community.flashcards.common.response.ActionType;
import org.hyperskill.community.flashcards.common.response.PermittedAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryMapperTest {

    CategoryMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CategoryMapper("user");
    }

    @Test
    void permittedUser_mapsCorrectly() {
        var category = createCategory("user", "rwd");
        var dto = mapper.categoryToCategoryDto(category);
        assertEquals(category.id(), dto.id());
        assertEquals(category.name(), dto.name());
        var expectedPermissions = Set.of(
                new PermittedAction(ActionType.READ, "/api/categories/12345"),
                new PermittedAction(ActionType.WRITE, "/api/categories/12345"),
                new PermittedAction(ActionType.DELETE, "/api/categories/12345"));
        assertEquals(expectedPermissions, dto.actions());
    }

    @Test
    void notPermittedUser_mapThrows() {
        var category = createCategory("other", "rw");
        assertThrows(IllegalStateException.class, () -> mapper.categoryToCategoryDto(category));
    }

    private Category createCategory( String username, String permissions) {
        return new Category("12345", "test", Set.of(new CategoryAccess(username, permissions)));
    }

}
