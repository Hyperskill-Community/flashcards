package org.hyperskill.community.flashcards.category.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateRequest(@NotBlank String name) { }
