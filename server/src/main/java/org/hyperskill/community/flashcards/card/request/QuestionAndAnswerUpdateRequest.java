package org.hyperskill.community.flashcards.card.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class QuestionAndAnswerUpdateRequest extends BaseCardUpdateRequest implements CardUpdateRequest {
    @NotBlank
    private String answer;
}
