package org.hyperskill.community.flashcards.category.mapper;

import lombok.extern.slf4j.Slf4j;
import org.hyperskill.community.flashcards.category.CategoryDto;
import org.hyperskill.community.flashcards.category.CategoryPageResponse;
import org.hyperskill.community.flashcards.category.model.Category;
import org.hyperskill.community.flashcards.category.model.CategoryAccess;
import org.springframework.data.domain.Page;

import java.util.Objects;

@Slf4j
public class CategoryMapper {
    private final String currentUserName;
    public CategoryMapper(String currentUserName) {
        Objects.requireNonNull(currentUserName, "Username cannot be null");
        this.currentUserName = currentUserName;
    }

    public CategoryDto categoryToCategoryDto(Category category) {
        var permissions = category.access().stream()
                .filter(access -> access.username().equals(currentUserName))
                .map(CategoryAccess::permission)
                .findAny()
                .orElse(null);

        if (permissions == null) {
            log.error("No permissions found for user '{}' in category '{}'", currentUserName, category.id());
            var message = "User %s must have permission to view category %s".formatted(currentUserName, category.id());
            throw new IllegalStateException(message);
        }

        return new CategoryDto(
                category.id(),
                category.name(),
                permissions
        );
    }

    public CategoryPageResponse categoryPageToCategoryPageResponse(Page<Category> page) {
        return new CategoryPageResponse(
                page.isFirst(),
                page.isLast(),
                page.getNumber(),
                page.getTotalPages(),
                page.stream().map(this::categoryToCategoryDto).toList()
        );
    }
}
