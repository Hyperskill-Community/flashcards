package org.hyperskill.community.flashcards.card.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
public sealed abstract class CardResponse permits QuestionAndAnswerDto, SingleChoiceQuizDto, MultipleChoiceQuizDto {
    protected final String id;
    protected final String question;
    protected final Set<String> tags;
    protected final String type;
    protected final Instant createdAt;
    protected final Set<PermittedAction> actions;

    public CardResponse(String id, String question, Set<String> tags, String type,
                        Instant createdAt, Set<PermittedAction> actions) {
        this.id = id;
        this.question = question;
        this.tags = tags;
        this.type = type;
        this.createdAt = createdAt;
        this.actions = actions;
    }
}
