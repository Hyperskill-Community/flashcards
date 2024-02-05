package org.hyperskill.community.flashcards.card.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public final class QuestionAndAnswerDto extends CardResponse {
    private final String answer;
}
