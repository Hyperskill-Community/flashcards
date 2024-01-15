package org.hyperskill.community.flashcards.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperskill.community.flashcards.category.mapper.CategoryMapper;
import org.hyperskill.community.flashcards.category.request.CategoryCreateRequest;
import org.hyperskill.community.flashcards.category.request.CategoryUpdateRequest;
import org.hyperskill.community.flashcards.category.response.CategoryDto;
import org.hyperskill.community.flashcards.category.response.CategoryPageResponse;
import org.hyperskill.community.flashcards.common.AuthenticationResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(path = "/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;
    private final AuthenticationResolver authenticationResolver;

    @GetMapping
    public CategoryPageResponse getAllCategories(
            @Valid @Min(0) @RequestParam(name = "page", required = false, defaultValue = "0") int page) {

        var username = authenticationResolver.resolveUsername();
        var categories = categoryService.getCategories(username, page);
        var mapper = new CategoryMapper(username);
        return mapper.categoryPageToCategoryPageResponse(categories);
    }

    @GetMapping(path = "/{categoryId}")
    public CategoryDto getCategory(@PathVariable String categoryId) {

        var username = authenticationResolver.resolveUsername();
        var category = categoryService.findById(username, categoryId);
        var mapper = new CategoryMapper(username);
        return mapper.categoryToCategoryDto(category);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CategoryCreateRequest request) {

        var username = authenticationResolver.resolveUsername();
        var categoryId = categoryService.createCategory(username, request);
        var uri = URI.create("/api/categories/" + categoryId);

        log.debug("User '{}' created a new category '{}' created", username, uri);

        return ResponseEntity
                .created(uri)
                .build();
    }

    @PutMapping(path = "/{categoryId}")
    public CategoryDto updateCategory(@PathVariable String categoryId,
                                      @Valid @RequestBody CategoryUpdateRequest request) {

        var username = authenticationResolver.resolveUsername();
        var updatedCategory = categoryService.updateById(username, categoryId, request);

        log.debug("User '{}' updated category '{}'", username, categoryId);

        var mapper = new CategoryMapper(username);
        return mapper.categoryToCategoryDto(updatedCategory);
    }

    @DeleteMapping(path = "/{categoryId}")
    public void deleteCategory(@PathVariable String categoryId) {

        var username = authenticationResolver.resolveUsername();
        categoryService.deleteById(username, categoryId);

        log.debug("User '{}' deleted category '{}'", username, categoryId);
    }
}
