package org.hyperskill.community.flashcards.category.response;

import org.hyperskill.community.flashcards.common.response.PermittedAction;

import java.util.Set;

public record CategoryDto(
        String id,
        String name,
        String description,
        Set<PermittedAction> actions
) {
}
