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
        @JsonSubTypes.Type(value = QuestionAndAnswerUpdateRequest.class, name = "qna"),
        @JsonSubTypes.Type(value = SingleChoiceQuizUpdateRequest.class, name = "scq"),
        @JsonSubTypes.Type(value = MultipleChoiceQuizUpdateRequest.class, name = "mcq"),
})
public sealed interface CardUpdateRequest permits
        QuestionAndAnswerUpdateRequest,
        SingleChoiceQuizUpdateRequest,
        MultipleChoiceQuizUpdateRequest {
}
