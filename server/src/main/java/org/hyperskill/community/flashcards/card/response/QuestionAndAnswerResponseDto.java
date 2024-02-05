package org.hyperskill.community.flashcards.card.response;

import lombok.Builder;
import org.hyperskill.community.flashcards.common.response.PermittedAction;

import java.time.Instant;
import java.util.Set;

@Builder
public record QuestionAndAnswerResponseDto(
        String id,
        String title,
        String question,
        Set<String> tags,
        CardType type,
        Instant createdAt,
        Set<PermittedAction> actions,
        String answer
) implements CardResponse {
}
