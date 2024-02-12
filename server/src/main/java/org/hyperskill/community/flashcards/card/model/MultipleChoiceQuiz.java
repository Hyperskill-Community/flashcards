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
 * Represents a multiple choice quiz card, i.e. where the user is provided with
 * multiple options of which at least one is correct. Commonly visualized as
 * having options with checkboxes
 */
@Builder
@TypeAlias("MCQ")
@Document
public record MultipleChoiceQuiz(
    @Id String id,
    String title,
    Set<String> tags,
    String question,
    @CreatedDate Instant createdAt,
    @Transient String permissions,
    List<String> options,
    List<Integer> correctOptions
)  implements Card {

    public MultipleChoiceQuiz setPermissions(String permissions) {
        return new MultipleChoiceQuiz(id, title, tags, question, createdAt, permissions, options, correctOptions);
    }

    @PersistenceCreator
    @SuppressWarnings("unused")
    public MultipleChoiceQuiz(String id, String title, Set<String> tags, String question, List<String> options,
                                     List<Integer> correctOptions, Instant createdAt) {
        this(id, title, tags, question, createdAt, null, options, correctOptions);
    }
}
