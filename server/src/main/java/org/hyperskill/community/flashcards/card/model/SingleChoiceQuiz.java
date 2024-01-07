package org.hyperskill.community.flashcards.card.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

/**
 * Represents a single choice quiz card, i.e. where the user is provided with
 * multiple options of which only one is correct. Commonly visualized as
 * having options with radial buttons
 */
@Getter
@Setter
@ToString(callSuper = true)
@TypeAlias("SingleChoiceQuiz")
public class SingleChoiceQuiz extends Card {
    private List<String> options;
    private Integer correctOption;

    public SingleChoiceQuiz() {
    }

    @Builder
    public SingleChoiceQuiz(String author, boolean isPublic, String title, String category,
                            String question, List<String> options, Integer correctOption) {
        super(author, isPublic, title, category, question);
        this.options = options;
        this.correctOption = correctOption;
    }
}
