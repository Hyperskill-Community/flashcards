package org.hyperskill.community.flashcards.card.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
public abstract sealed class Card permits QuestionAndAnswerCard, SingleChoiceQuiz, MultipleChoiceQuiz {
    @Id
    protected String id;
    protected String author;
    protected String title;
    protected Set<String> tags;
    protected String question;
    @CreatedDate
    protected Instant createdAt;

    public Card() { }

    protected Card(String author, String title, Set<String> tags, String question) {
        this.author = author;
        this.title = title;
        this.tags = tags == null ? new HashSet<>() : tags;
        this.question = question;
    }
}
