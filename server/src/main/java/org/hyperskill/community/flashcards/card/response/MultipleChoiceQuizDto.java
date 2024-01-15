package org.hyperskill.community.flashcards.card.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hyperskill.community.flashcards.common.response.PermittedAction;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public final class MultipleChoiceQuizDto extends CardResponse {
    private final List<String> options;
    private final List<Integer> correctOptions;

    @Builder
    public MultipleChoiceQuizDto(String id, String question, Set<String> tags, String type, List<String> options,
                                 List<Integer> correctOptions, Instant createdAt, Set<PermittedAction> actions) {
        super(id, question, tags, type, createdAt, actions);
        this.options = options;
        this.correctOptions = correctOptions;
    }
}
