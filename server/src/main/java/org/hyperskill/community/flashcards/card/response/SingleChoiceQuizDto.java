package org.hyperskill.community.flashcards.card.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public final class SingleChoiceQuizDto extends CardResponse {
    private final List<String> options;
    private final Integer correctOption;
}
