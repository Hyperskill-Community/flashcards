package org.hyperskill.community.flashcards.card;

import org.hyperskill.community.flashcards.card.model.Card;

import java.util.List;

public record CardPageResponse(
        boolean isFirst,
        boolean isLast,
        int currentPage,
        int totalPages,
        // fixme change to DTO
        List<Card> cards
) {
}
