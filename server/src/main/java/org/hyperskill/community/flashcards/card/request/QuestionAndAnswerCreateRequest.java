package org.hyperskill.community.flashcards.card.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class QuestionAndAnswerCreateRequest extends BaseCardCreateRequest implements CardCreateRequest {
    @NotBlank
    private String answer;
}
