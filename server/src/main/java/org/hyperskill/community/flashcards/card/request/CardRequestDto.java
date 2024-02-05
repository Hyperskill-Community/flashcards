package org.hyperskill.community.flashcards.card.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Set;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = QuestionAndAnswerRequestDto.class, name = "qna"),
        @JsonSubTypes.Type(value = SingleChoiceQuizRequestDto.class, name = "scq"),
        @JsonSubTypes.Type(value = MultipleChoiceQuizRequestDto.class, name = "mcq"),
})
public sealed interface CardRequestDto permits MultipleChoiceQuizRequestDto, QuestionAndAnswerRequestDto, SingleChoiceQuizRequestDto {

    String title();

    Set<String> tags();

    String question();
}
