package org.hyperskill.community.flashcards.dataio;

import lombok.RequiredArgsConstructor;
import org.hyperskill.community.flashcards.card.CardService;
import org.hyperskill.community.flashcards.category.CategoryService;
import org.hyperskill.community.flashcards.category.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final CategoryService categoryService;
    private final CardService cardService;

    public UserExport retrieveUserData(String username) {
        List<CategoryExport> categories = categoryService.getCategories(username)
                .stream().map(this::findExportData).toList();
        return new UserExport(username, categories);
    }

    private CategoryExport findExportData(Category category) {
        return CategoryExport.builder()
                .name(category.name())
                .description(category.description())
                .access(category.access())
                .cards(cardService.getCards(category.name()))
                .build();
    }
}
