package org.hyperskill.community.flashcards.card.mapper;

import org.hyperskill.community.flashcards.card.model.Card;
import org.hyperskill.community.flashcards.card.model.MultipleChoiceQuiz;
import org.hyperskill.community.flashcards.card.model.QuestionAndAnswer;
import org.hyperskill.community.flashcards.card.model.SingleChoiceQuiz;
import org.hyperskill.community.flashcards.card.response.CardResponse;
import org.hyperskill.community.flashcards.card.response.MultipleChoiceQuizDto;
import org.hyperskill.community.flashcards.card.response.QuestionAndAnswerDto;
import org.hyperskill.community.flashcards.card.response.SingleChoiceQuizDto;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CardMapper {
    public CardResponse map(Card card) {
        return switch (card) {
            case QuestionAndAnswer qna -> toDto(qna);
            case SingleChoiceQuiz scq -> toDto(scq);
            case MultipleChoiceQuiz mcq -> toDto(mcq);
        };
    }

    private QuestionAndAnswerDto toDto(QuestionAndAnswer card) {
        return QuestionAndAnswerDto.builder()
                .id(card.getId())
                .type("qna")
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .tags(card.getTags())
                .actions(Set.of())
                .createdAt(card.getCreatedAt())
                .build();
    }

    private SingleChoiceQuizDto toDto(SingleChoiceQuiz card) {
        return SingleChoiceQuizDto.builder()
                .id(card.getId())
                .type("scq")
                .question(card.getQuestion())
                .options(card.getOptions())
                .correctOption(card.getCorrectOption())
                .tags(card.getTags())
                .actions(Set.of())
                .createdAt(card.getCreatedAt())
                .build();
    }

    private MultipleChoiceQuizDto toDto(MultipleChoiceQuiz card) {
        return MultipleChoiceQuizDto.builder()
                .id(card.getId())
                .type("mcq")
                .question(card.getQuestion())
                .options(card.getOptions())
                .correctOptions(card.getCorrectOptions())
                .tags(card.getTags())
                .actions(Set.of())
                .createdAt(card.getCreatedAt())
                .build();
    }
}
