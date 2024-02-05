package org.hyperskill.community.flashcards.card.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
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
@SuperBuilder
@TypeAlias("MCQ")
public final class MultipleChoiceQuiz extends Card {
    private List<String> options;
    private List<Integer> correctOptions;
}
