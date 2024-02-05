package org.hyperskill.community.flashcards.card.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder

@TypeAlias("QNA")
public final class QuestionAndAnswer extends Card {
    private String answer;
}
