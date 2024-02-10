package org.hyperskill.community.flashcards.card;

import org.hyperskill.community.flashcards.card.request.MultipleChoiceQuizRequestDto;
import org.hyperskill.community.flashcards.card.request.QuestionAndAnswerRequestDto;
import org.hyperskill.community.flashcards.card.request.SingleChoiceQuizRequestDto;
import org.hyperskill.community.flashcards.validation.JpaUnitTestValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardControllerValidationUnitTest {

    JpaUnitTestValidator<MultipleChoiceQuizRequestDto> mcqValidator
            = new JpaUnitTestValidator<>(this::getValidMcqRequest, MultipleChoiceQuizRequestDto.class);

    MultipleChoiceQuizRequestDto getValidMcqRequest() {
        return MultipleChoiceQuizRequestDto.builder()
                .title("title")
                .question("question")
                .options(List.of("option1", "option2"))
                .tags(Set.of("tag1", "tag2"))
                .correctOptions(List.of(0))
                .build();
    }

    static Stream<Arguments> whenValidMcqRequest_NoError() {
        return Stream.of(
                Arguments.of("title", "a"),
                Arguments.of("title", "a+-3§"),
                Arguments.of("question", "a"),
                Arguments.of("question", "a+-3§"),
                Arguments.of("options", List.of("a")),
                Arguments.of("options", List.of("a", "b", "c")),
                Arguments.of("tags", Set.of("a")),
                Arguments.of("tags", Set.of("a", "b", "c")),
                Arguments.of("correctOptions", List.of(0)),
                Arguments.of("correctOptions", List.of(0, 1)),
                Arguments.of("correctOptions", List.of(0, 1, 2))
        );
    }

    @ParameterizedTest
    @MethodSource
    void whenValidMcqRequest_NoError(String fieldName, Object validValue) throws Exception {
        assertTrue(mcqValidator.getConstraintViolationsOnUpdate(fieldName, validValue).isEmpty());
    }

    static Stream<Arguments> whenInvalidMcqRequest_ValidationError() {
        return Stream.of(
                Arguments.of("title", ""),
                Arguments.of("title", "  "),
                Arguments.of("title", null),
                Arguments.of("question", ""),
                Arguments.of("question", "  "),
                Arguments.of("question", null),
                Arguments.of("options", List.of()),
                Arguments.of("options", null),
                Arguments.of("options", List.of(" ", "a")),
                Arguments.of("options", List.of("a", " ")),
                Arguments.of("tags", null),
                Arguments.of("tags", Set.of("")),
                Arguments.of("tags", Set.of(" ", "a")),
                Arguments.of("tags", Set.of("a", " ")),
                Arguments.of("correctOptions", List.of()),
                Arguments.of("correctOptions", List.of(-1)),
                Arguments.of("correctOptions", null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void whenInvalidMcqRequest_ValidationError(String fieldName, Object validValue) throws Exception {
        assertFalse(mcqValidator.getConstraintViolationsOnUpdate(fieldName, validValue).isEmpty());
    }

    JpaUnitTestValidator<SingleChoiceQuizRequestDto> scqValidator
            = new JpaUnitTestValidator<>(this::getValidScqRequest, SingleChoiceQuizRequestDto.class);

    SingleChoiceQuizRequestDto getValidScqRequest() {
        return SingleChoiceQuizRequestDto.builder()
                .title("title")
                .question("question")
                .options(List.of("option1", "option2"))
                .tags(Set.of("tag1", "tag2"))
                .correctOption(0)
                .build();
    }

    static Stream<Arguments> whenValidScqRequest_NoError() {
        return Stream.of(
                Arguments.of("title", "a"),
                Arguments.of("title", "a+-3§"),
                Arguments.of("question", "a"),
                Arguments.of("question", "a+-3§"),
                Arguments.of("options", List.of("a")),
                Arguments.of("options", List.of("a", "b", "c")),
                Arguments.of("tags", Set.of("a")),
                Arguments.of("tags", Set.of("a", "b", "c")),
                Arguments.of("correctOption", 0),
                Arguments.of("correctOption", 2)
        );
    }

    @ParameterizedTest
    @MethodSource
    void whenValidScqRequest_NoError(String fieldName, Object validValue) throws Exception {
        assertTrue(scqValidator.getConstraintViolationsOnUpdate(fieldName, validValue).isEmpty());
    }

    static Stream<Arguments> whenInvalidScqRequest_ValidationError() {
        return Stream.of(
                Arguments.of("title", ""),
                Arguments.of("title", "  "),
                Arguments.of("title", null),
                Arguments.of("question", ""),
                Arguments.of("question", "  "),
                Arguments.of("question", null),
                Arguments.of("options", List.of()),
                Arguments.of("options", null),
                Arguments.of("options", List.of(" ", "a")),
                Arguments.of("options", List.of("a", " ")),
                Arguments.of("tags", null),
                Arguments.of("tags", Set.of("")),
                Arguments.of("tags", Set.of(" ", "a")),
                Arguments.of("tags", Set.of("a", " ")),
                Arguments.of("correctOption", null),
                Arguments.of("correctOption", -1)
        );
    }

    @ParameterizedTest
    @MethodSource
    void whenInvalidScqRequest_ValidationError(String fieldName, Object validValue) throws Exception {
        assertFalse(scqValidator.getConstraintViolationsOnUpdate(fieldName, validValue).isEmpty());
    }

    JpaUnitTestValidator<QuestionAndAnswerRequestDto> qnaValidator
            = new JpaUnitTestValidator<>(this::getValidQnaRequest, QuestionAndAnswerRequestDto.class);

    QuestionAndAnswerRequestDto getValidQnaRequest() {
        return QuestionAndAnswerRequestDto.builder()
                .title("title")
                .question("question")
                .answer("answer")
                .tags(Set.of("tag1", "tag2"))
                .build();
    }

    static Stream<Arguments> whenValidQnaRequest_NoError() {
        return Stream.of(
                Arguments.of("title", "a"),
                Arguments.of("title", "a+-3§"),
                Arguments.of("question", "a"),
                Arguments.of("question", "a+-3§"),
                Arguments.of("answer", "a"),
                Arguments.of("answer", "a+-3§"),
                Arguments.of("tags", Set.of("a")),
                Arguments.of("tags", Set.of("a", "b", "c"))
        );
    }

    @ParameterizedTest
    @MethodSource
    void whenValidQnaRequest_NoError(String fieldName, Object validValue) throws Exception {
        assertTrue(qnaValidator.getConstraintViolationsOnUpdate(fieldName, validValue).isEmpty());
    }

    static Stream<Arguments> whenInvalidQnaRequest_ValidationError() {
        return Stream.of(
                Arguments.of("title", ""),
                Arguments.of("title", "  "),
                Arguments.of("title", null),
                Arguments.of("question", ""),
                Arguments.of("question", "  "),
                Arguments.of("question", null),
                Arguments.of("answer", ""),
                Arguments.of("answer", "  "),
                Arguments.of("answer", null),
                Arguments.of("tags", null),
                Arguments.of("tags", Set.of("")),
                Arguments.of("tags", Set.of(" ", "a")),
                Arguments.of("tags", Set.of("a", " "))
        );
    }

    @ParameterizedTest
    @MethodSource
    void whenInvalidQnaRequest_ValidationError(String fieldName, Object validValue) throws Exception {
        assertFalse(qnaValidator.getConstraintViolationsOnUpdate(fieldName, validValue).isEmpty());
    }

}
