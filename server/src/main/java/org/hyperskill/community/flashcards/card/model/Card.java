package org.hyperskill.community.flashcards.card.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Basic class for all flashcards, contains fields shared among all subclasses
 */
@Getter
@Setter
@Document
@ToString
public abstract class Card {
    @Id
    protected String id;
    protected String author;
    protected boolean isPublic;
    protected String title;
    protected String category;
    protected String question;
    @CreatedDate
    protected Instant createdAt;

    public Card() { }

    protected Card(String author, boolean isPublic, String title, String category, String question) {
        this.author = author;
        this.isPublic = isPublic;
        this.title = title;
        this.category = category;
        this.question = question;
    }
}
