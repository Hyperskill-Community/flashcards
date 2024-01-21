package org.hyperskill.community.flashcards.card.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class BaseCardCreateRequest {
    @NotBlank
    protected String title;
    @NotNull
    protected Set<@NotBlank String> tags;
    @NotBlank
    protected String question;
}
