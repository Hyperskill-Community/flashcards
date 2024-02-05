package org.hyperskill.community.flashcards.card.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hyperskill.community.flashcards.card.mapper.MongoDateConverter;
import org.hyperskill.community.flashcards.card.mapper.MongoObjectIdConverter;
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
@Document
@ToString
@SuperBuilder
public abstract sealed class Card permits QuestionAndAnswer, SingleChoiceQuiz, MultipleChoiceQuiz {
    @Id
    @JsonDeserialize(using = MongoObjectIdConverter.class)
    @JsonAlias({"id", "_id"})
    private String id;
    private String title;
    private Set<String> tags;
    private String question;
    @CreatedDate
    @JsonDeserialize(using = MongoDateConverter.class)
    private Instant createdAt;
    @Transient
    private String permissions;
}
