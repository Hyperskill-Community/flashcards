package org.hyperskill.community.flashcards.card.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public final class SingleChoiceQuizDto extends CardResponse {
    private final List<String> options;
    private final Integer correctOption;

    @Builder
    public SingleChoiceQuizDto(String id, String question, Set<String> tags, String type, List<String> options,
                               Integer correctOption, Instant createdAt, Set<PermittedAction> actions) {
        super(id, question, tags, type, createdAt, actions);
        this.options = options;
        this.correctOption = correctOption;
    }
}