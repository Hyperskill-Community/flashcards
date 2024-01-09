package org.hyperskill.community.flashcards.category;

import lombok.RequiredArgsConstructor;
import org.hyperskill.community.flashcards.category.mapper.CategoryMapper;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public CategoryPageResponse getAllCategories(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            Authentication authentication
    ) {
        // fixme
        var username = "test1@test.com";  // authentication.getName();
        var categories = categoryService.getCategories(page);
        var mapper = new CategoryMapper(username);
        return mapper.categoryPageToCategoryPageResponse(categories);
    }
}
