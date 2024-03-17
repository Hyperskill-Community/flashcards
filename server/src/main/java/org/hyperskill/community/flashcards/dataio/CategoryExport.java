package org.hyperskill.community.flashcards.dataio;

import lombok.Builder;
import org.hyperskill.community.flashcards.card.model.Card;
import org.hyperskill.community.flashcards.category.model.CategoryAccess;

import java.util.List;
import java.util.Set;

@Builder
public record CategoryExport(
        String name,
        String description,
        Set<CategoryAccess> access,
        List<Card> cards
) {
}
