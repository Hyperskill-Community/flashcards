package org.hyperskill.community.flashcards.card.response;

import lombok.Builder;
import org.hyperskill.community.flashcards.common.response.PermittedAction;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Builder
public record SingleChoiceQuizResponseDto(
        String id,
        String title,
        String question,
        Set<String> tags,
        CardType type,
        Instant createdAt,
        Set<PermittedAction> actions,
        List<String> options,
        Integer correctOption
) implements CardResponse {
}
