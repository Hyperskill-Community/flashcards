package org.hyperskill.community.flashcards.category;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hyperskill.community.flashcards.category.request.CategoryCreateRequest;
import org.hyperskill.community.flashcards.category.request.CategoryUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryControllerValidationUnitTest {

    Validator validator;

    @BeforeEach
    void setup() {
        try (var validatorFactory = Validation.buildDefaultValidatorFactory()) {
            this.validator = validatorFactory.getValidator();
        }
    }

    @Test
    void whenNullName_ValidationError()  {
        var createRequest = new CategoryCreateRequest(null);
        assertFalse(validator.validate(createRequest).isEmpty());
        var updateRequest = new CategoryUpdateRequest(null);
        assertFalse(validator.validate(updateRequest).isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    void whenBlankName_ValidationError(String name)  {
        var createRequest = new CategoryCreateRequest(name);
        assertFalse(validator.validate(createRequest).isEmpty());
        var updateRequest = new CategoryUpdateRequest(name);
        assertFalse(validator.validate(updateRequest).isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "category", "a+-3§"})
    void whenNonBlankName_ValidationOk(String name)  {
        var createRequest = new CategoryCreateRequest(name);
        assertTrue(validator.validate(createRequest).isEmpty());
        var updateRequest = new CategoryUpdateRequest(name);
        assertTrue(validator.validate(updateRequest).isEmpty());
    }
}
