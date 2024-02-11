package org.hyperskill.community.flashcards.card.response;

import org.hyperskill.community.flashcards.card.model.Card;
import org.hyperskill.community.flashcards.card.model.MultipleChoiceQuiz;
import org.hyperskill.community.flashcards.card.model.QuestionAndAnswer;
import org.hyperskill.community.flashcards.card.model.SingleChoiceQuiz;
import org.hyperskill.community.flashcards.card.request.CardRequest;
import org.hyperskill.community.flashcards.card.request.MultipleChoiceQuizRequestDto;
import org.hyperskill.community.flashcards.card.request.QuestionAndAnswerRequestDto;
import org.hyperskill.community.flashcards.card.request.SingleChoiceQuizRequestDto;

public enum CardType {
    QNA,
    SCQ,
    MCQ;

    public static CardType fromRequest(CardRequest request) {
        return switch (request) {
            case QuestionAndAnswerRequestDto ignore -> QNA;
            case SingleChoiceQuizRequestDto ignore -> SCQ;
            case MultipleChoiceQuizRequestDto ignore -> MCQ;
        };
    }

    public static CardType fromCard(Card card) {
        return switch (card) {
            case QuestionAndAnswer ignoredC -> CardType.QNA;
            case SingleChoiceQuiz ignoredC -> CardType.SCQ;
            case MultipleChoiceQuiz ignoredC -> CardType.MCQ;
        };
    }
}
