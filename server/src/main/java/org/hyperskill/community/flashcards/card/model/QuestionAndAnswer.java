package org.hyperskill.community.flashcards.card.model;

import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;

import java.time.Instant;
import java.util.Set;

@Builder
@TypeAlias("QNA")
public record QuestionAndAnswer(
        @Id String id,
        String title,
        Set<String> tags,
        String question,
        @CreatedDate Instant createdAt,
        @Transient String permissions,
        String answer
) implements Card {

    public QuestionAndAnswer setPermissions(String permissions) {
        return new QuestionAndAnswer(id, title, tags, question, createdAt, permissions, answer);
    }

    @PersistenceCreator
    @SuppressWarnings("unused")
    public QuestionAndAnswer(String id, String title, Set<String> tags, String question, String answer, Instant createdAt) {
        this(id, title, tags, question, createdAt, null, answer);
    }
}
