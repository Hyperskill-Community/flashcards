package org.hyperskill.community.flashcards.category;

import java.util.List;

public record CategoryPageResponse(
        boolean isFirst,
        boolean isLast,
        int currentPage,
        int totalPages,
        List<CategoryDto> categories
) {
}
