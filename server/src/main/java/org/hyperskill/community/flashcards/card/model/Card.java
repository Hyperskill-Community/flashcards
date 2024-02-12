package org.hyperskill.community.flashcards.card.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.Instant;
import java.util.Set;

/**
 * Basic class for all flashcards, contains fields shared among all subclasses
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = QuestionAndAnswer.class, name = "QNA"),
        @JsonSubTypes.Type(value = SingleChoiceQuiz.class, name = "SCQ"),
        @JsonSubTypes.Type(value = MultipleChoiceQuiz.class, name = "MCQ")
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
