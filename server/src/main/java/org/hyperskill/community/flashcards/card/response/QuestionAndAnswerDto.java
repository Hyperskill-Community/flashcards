package org.hyperskill.community.flashcards.card.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hyperskill.community.flashcards.common.response.PermittedAction;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
public final class QuestionAndAnswerDto extends CardResponse {
    private final String answer;

    @Builder
    public QuestionAndAnswerDto(String id, String title, String question, Set<String> tags, String type,
                                Instant createdAt, String answer, Set<PermittedAction> actions) {
        super(id, question, title, tags, type, createdAt, actions);
        this.answer = answer;
    }
}
