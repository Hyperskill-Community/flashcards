package org.hyperskill.community.flashcards.card.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hyperskill.community.flashcards.common.response.PermittedAction;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
public abstract sealed class CardResponse permits QuestionAndAnswerDto, SingleChoiceQuizDto, MultipleChoiceQuizDto {
    private final String id;
    private final String title;
    private final String question;
    private final Set<String> tags;
    private final String type;
    private final Instant createdAt;
    private final Set<PermittedAction> actions;
}
