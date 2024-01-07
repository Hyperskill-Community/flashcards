package org.hyperskill.community.flashcards.card.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

/**
 * Represents a multiple choice quiz card, i.e. where the user is provided with
 * multiple options of which at least one is correct. Commonly visualized as
 * having options with checkboxes
 */
@Getter
@Setter
@ToString(callSuper = true)
@TypeAlias("MultipleChoiceQuiz")
public class MultipleChoiceQuiz extends Card {
    private List<String> options;
    private List<Integer> correctOptions;

    public MultipleChoiceQuiz() {
    }

    @Builder
    public MultipleChoiceQuiz(String author, boolean isPublic, String title, String category,
                              String question, List<String> options, List<Integer> correctOptions) {
        super(author, isPublic, title, category, question);
        this.options = options;
        this.correctOptions = correctOptions;
    }
}
