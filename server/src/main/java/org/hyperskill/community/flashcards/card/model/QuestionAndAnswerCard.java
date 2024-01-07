package org.hyperskill.community.flashcards.card.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;

@Getter
@Setter
@ToString(callSuper = true)
@TypeAlias("QuestionAndAnswerCard")
public class QuestionAndAnswerCard extends Card {
    private String answer;

    public QuestionAndAnswerCard() { }

    @Builder
    public QuestionAndAnswerCard(String author, boolean isPublic, String title,
                                 String category, String question, String answer) {
        super(author, isPublic, title, category, question);
        this.answer = answer;
    }
}
