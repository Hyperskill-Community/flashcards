package org.hyperskill.community.flashcards.card.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;

import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
@TypeAlias("QuestionAndAnswerCard")
public final class QuestionAndAnswerCard extends Card {
    private String answer;

    public QuestionAndAnswerCard() { }

    @Builder
    public QuestionAndAnswerCard(String author, boolean isPublic, String title,
                                 Set<String> tags, String question, String answer) {
        super(author, isPublic, title, tags, question);
        this.answer = answer;
    }
}
