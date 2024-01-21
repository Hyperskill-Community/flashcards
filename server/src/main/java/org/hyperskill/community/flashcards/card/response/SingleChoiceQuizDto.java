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
public final class SingleChoiceQuizDto extends CardResponse {
    private final List<String> options;
    private final Integer correctOption;

    @Builder
    public SingleChoiceQuizDto(String id, String question, String title, Set<String> tags, String type,
                               List<String> options, Integer correctOption, Instant createdAt,
                               Set<PermittedAction> actions) {
        super(id, question, title, tags, type, createdAt, actions);
        this.options = options;
        this.correctOption = correctOption;
    }
}
