package org.hyperskill.community.flashcards.card;

import org.hyperskill.community.flashcards.card.response.CardResponse;

import java.util.List;

public record CardPageResponse(
        boolean isFirst,
        boolean isLast,
        int currentPage,
        int totalPages,
        List<CardResponse> cards
) {
}
