package org.hyperskill.community.flashcards.card.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hyperskill.community.flashcards.card.mapper.MongoDateConverter;
import org.hyperskill.community.flashcards.card.mapper.MongoObjectIdConverter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Basic class for all flashcards, contains fields shared among all subclasses
 */
@Getter
@Setter
@Document
@ToString
public sealed class Card permits QuestionAndAnswerCard, SingleChoiceQuiz, MultipleChoiceQuiz {
    @Id
    @JsonDeserialize(using = MongoObjectIdConverter.class)
    @JsonAlias({"id", "_id"})
    protected String id;
    protected String title;
    protected Set<String> tags;
    protected String question;
    @CreatedDate
    @JsonDeserialize(using = MongoDateConverter.class)
    protected Instant createdAt;

    protected Card(String title, Set<String> tags, String question) {
        this.title = title;
        this.tags = tags == null ? new HashSet<>() : tags;
        this.question = question;
    }
}
