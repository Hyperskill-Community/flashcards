package org.hyperskill.community.flashcards.card.response;

import org.hyperskill.community.flashcards.common.response.PermittedAction;

import java.time.Instant;
import java.util.Set;

public sealed interface CardResponse permits QuestionAndAnswerResponseDto, SingleChoiceQuizResponseDto, MultipleChoiceQuizResponseDto {
    String id();
    String title();
    String question();
    Set<String> tags();
    CardType type();
    Instant createdAt();
    Set<PermittedAction> actions();
}
