package org.hyperskill.community.flashcards.card.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder
public record SingleChoiceQuizRequestDto(
        String title,
        @NotNull Set<@NotBlank String> tags,
        @NotBlank String question,
        @NotNull @NotEmpty List<@NotBlank String> options,
        @NotNull @Min(0) Integer correctOption
) implements CardRequest {
}
