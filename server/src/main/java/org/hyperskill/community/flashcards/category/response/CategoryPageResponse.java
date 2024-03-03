package org.hyperskill.community.flashcards.category.response;

import java.util.List;

public record CategoryPageResponse(
        boolean isLast,
        int currentPage,
        int totalPages,
        long totalElements,
        List<CategoryDto> categories
) {
}
