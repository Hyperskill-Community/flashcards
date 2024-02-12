package org.hyperskill.community.flashcards.card.model;

import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * Represents a single choice quiz card, i.e. where the user is provided with
 * multiple options of which only one is correct. Commonly visualized as
 * having options with radial buttons
 */
@Builder
@TypeAlias("SCQ")
@Document
public record SingleChoiceQuiz(
        @Id String id,
        String title,
        Set<String> tags,
        String question,
        @CreatedDate Instant createdAt,
        @Transient String permissions,
        List<String> options,
        Integer correctOption
) implements Card {

    public SingleChoiceQuiz setPermissions(String permissions) {
        return new SingleChoiceQuiz(id, title, tags, question, createdAt, permissions, options, correctOption);
    }

    @PersistenceCreator
    @SuppressWarnings("unused")
    public SingleChoiceQuiz(String id, String title, Set<String> tags, String question, List<String> options,
                                   Integer correctOption, Instant createdAt) {
        this(id, title, tags, question, createdAt, null, options, correctOption);
    }
}

