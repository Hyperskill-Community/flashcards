package org.hyperskill.community.flashcards.card.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record QuestionAndAnswerRequestDto(
        @NotBlank String title,
        @NotNull Set<@NotBlank String>tags,
        @NotBlank String question,
        @NotBlank String answer
) implements CardRequestDto {
}
