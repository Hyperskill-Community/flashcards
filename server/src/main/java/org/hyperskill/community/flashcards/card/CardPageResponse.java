package org.hyperskill.community.flashcards.card;

import java.util.List;

/**
 * Represents a page of cards retrieved.
 * @param <I> type of the Item in the card page
 */
public record CardPageResponse<I>(
        boolean isFirst,
        boolean isLast,
        int currentPage,
        int totalPages,
        List<I> cards
) {
}
