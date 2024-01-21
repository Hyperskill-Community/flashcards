package org.hyperskill.community.flashcards.card.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public final class MultipleChoiceQuizCreateRequest extends BaseCardCreateRequest implements CardCreateRequest {
    @NotNull
    @NotEmpty
    private List<@NotBlank String> options;
    @NotNull
    @NotEmpty
    private List<@NotNull @Min(0) Integer> correctOptions;
}
