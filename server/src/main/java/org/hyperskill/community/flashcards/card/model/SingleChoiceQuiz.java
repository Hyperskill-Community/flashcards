package org.hyperskill.community.flashcards.card.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
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
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@SuperBuilder
@TypeAlias("SCQ")
public final class SingleChoiceQuiz extends Card {
    private List<String> options;
    private Integer correctOption;
}
