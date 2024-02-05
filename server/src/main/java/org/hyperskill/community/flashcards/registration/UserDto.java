package org.hyperskill.community.flashcards.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * immutable web-layer DTO as carrier for user register requests.
 */
public record UserDto(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password
) {
}
