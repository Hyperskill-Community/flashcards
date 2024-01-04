package org.hyperskill.community.flashcards.web;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.hyperskill.community.flashcards.registration.UserDto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RegisterControllerValidationUnitTest {

    JpaUnitTestValidator<UserDto> validator =
            new JpaUnitTestValidator<>(this::getValidUser, UserDto.class);

    UserDto getValidUser() {
        return new UserDto("hans.wurst@google.com", "long_password");
    }

    @ParameterizedTest
    @MethodSource
    void whenValidUserDto_NoError(String fieldName, Object validValue) throws Exception {
        assertTrue(validator.getConstraintViolationsOnUpdate(fieldName, validValue).isEmpty());
    }

    static Stream<Arguments> whenValidUserDto_NoError() {
        return Stream.of(Arguments.of("email", "a@b.c"), Arguments.of("email", "hans@lmu.de"),
                Arguments.of("email", "hans_josef.schmitz.ext4@main_google.com"),
                Arguments.of("password", "12345678"),
                Arguments.of("password", "12345678_very_long_one!\"§$%&/()=?.,-,),"));
    }

    @ParameterizedTest
    @MethodSource
    void whenInvalidUserDto_ValidationError(String fieldName, Object validValue) throws Exception {
        assertFalse(validator.getConstraintViolationsOnUpdate(fieldName, validValue).isEmpty());
    }

    static Stream<Arguments> whenInvalidUserDto_ValidationError() {
        return Stream.of(Arguments.of("email", " "), Arguments.of("email", ""),
                Arguments.of("email", null), Arguments.of("email", "a@b"),
                Arguments.of("email", "hans@lmu."),
                Arguments.of("email", "hans.josef.schmitz.ext4@main_google.com"),
                Arguments.of("email", "hans-josef@main_google.com"),
                Arguments.of("email", ".ext4@main_google.com"),
                Arguments.of("email", "hans.josef@.com"),
                Arguments.of("email", "hans..josef@google.com"),
                Arguments.of("email", "hansjosefgoogle.com"), Arguments.of("email", "hansjosef@dd"),
                Arguments.of("password", "1234567"), Arguments.of("password", "        "),
                Arguments.of("password", "  "), Arguments.of("password", ""),
                Arguments.of("password", null));
    }
}
