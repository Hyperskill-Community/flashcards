package org.hyperskill.community.flashcards.card.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Set;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = QuestionAndAnswerRequestDto.class, name = "QNA"),
        @JsonSubTypes.Type(value = SingleChoiceQuizRequestDto.class, name = "SCQ"),
        @JsonSubTypes.Type(value = MultipleChoiceQuizRequestDto.class, name = "MCQ"),
})
public sealed interface CardRequest permits MultipleChoiceQuizRequestDto, QuestionAndAnswerRequestDto, SingleChoiceQuizRequestDto {

    String title();

    Set<String> tags();

    String question();
}
