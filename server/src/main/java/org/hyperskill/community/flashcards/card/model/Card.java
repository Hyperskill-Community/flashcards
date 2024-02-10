package org.hyperskill.community.flashcards.card.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Set;

/**
 * Basic class for all flashcards, contains fields shared among all subclasses
 */

@Document
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = QuestionAndAnswer.class, name = "qna"),
        @JsonSubTypes.Type(value = SingleChoiceQuiz.class, name = "scq"),
        @JsonSubTypes.Type(value = MultipleChoiceQuiz.class, name = "mcq"),
})
public sealed interface Card permits QuestionAndAnswer, SingleChoiceQuiz, MultipleChoiceQuiz {

    String ID_KEY = "_id";

    String id();

    String title();

    Set<String> tags();

    String question();

    Instant createdAt();

    String permissions();

    /**
     * updates permissions of the card.
     * @param permissions new permissions
     * @return new instance of the card with updated permissions
     */
    Card setPermissions(String permissions);
}
