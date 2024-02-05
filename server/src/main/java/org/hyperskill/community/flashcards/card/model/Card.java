package org.hyperskill.community.flashcards.card.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Set;

/**
 * Basic class for all flashcards, contains fields shared among all subclasses
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
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
public abstract sealed class Card permits QuestionAndAnswer, SingleChoiceQuiz, MultipleChoiceQuiz {
    @Id
    private String id;
    private String title;
    private Set<String> tags;
    private String question;
    @CreatedDate
    private Instant createdAt;
    @Transient
    private String permissions;
}
