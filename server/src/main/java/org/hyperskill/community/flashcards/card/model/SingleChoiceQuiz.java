package org.hyperskill.community.flashcards.card.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;
import java.util.Set;

/**
 * Represents a single choice quiz card, i.e. where the user is provided with
 * multiple options of which only one is correct. Commonly visualized as
 * having options with radial buttons
 */
@Getter
@Setter
@ToString(callSuper = true)
@TypeAlias("SingleChoiceQuiz")
public final class SingleChoiceQuiz extends Card {
    private List<String> options;
    private Integer correctOption;

    public SingleChoiceQuiz() {
        super(null, null, null);
    }

    @Builder
    public SingleChoiceQuiz(String title, Set<String> tags, String question,
                            List<String> options, Integer correctOption) {
        super(title, tags, question);
        this.options = options;
        this.correctOption = correctOption;
    }
}
