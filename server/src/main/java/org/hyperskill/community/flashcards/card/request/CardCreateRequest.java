package org.hyperskill.community.flashcards.card.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = QuestionAndAnswerCreateRequest.class, name = "qna"),
        @JsonSubTypes.Type(value = SingleChoiceQuizCreateRequest.class, name = "scq"),
        @JsonSubTypes.Type(value = MultipleChoiceQuizCreateRequest.class, name = "mcq"),
})
public sealed interface CardCreateRequest permits
        QuestionAndAnswerCreateRequest,
        SingleChoiceQuizCreateRequest,
        MultipleChoiceQuizCreateRequest {
}
