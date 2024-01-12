package org.hyperskill.community.flashcards.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperskill.community.flashcards.category.mapper.CategoryMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public CategoryPageResponse getAllCategories(
            @Valid @Min(0) @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @AuthenticationPrincipal OidcUser oidcUser) {

        var username = oidcUser.getSubject();
        var categories = categoryService.getCategories(username, page);
        var mapper = new CategoryMapper(username);
        return mapper.categoryPageToCategoryPageResponse(categories);
    }
}
