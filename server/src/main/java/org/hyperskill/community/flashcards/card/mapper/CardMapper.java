package org.hyperskill.community.flashcards.card.mapper;

import org.hyperskill.community.flashcards.card.model.Card;
import org.hyperskill.community.flashcards.card.model.MultipleChoiceQuiz;
import org.hyperskill.community.flashcards.card.model.QuestionAndAnswer;
import org.hyperskill.community.flashcards.card.model.SingleChoiceQuiz;
import org.hyperskill.community.flashcards.card.request.CardCreateRequest;
import org.hyperskill.community.flashcards.card.request.MultipleChoiceQuizCreateRequest;
import org.hyperskill.community.flashcards.card.request.QuestionAndAnswerCreateRequest;
import org.hyperskill.community.flashcards.card.request.SingleChoiceQuizCreateRequest;
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

    public <T extends CardCreateRequest> Card toDocument(T request) {
        return switch (request) {
            case QuestionAndAnswerCreateRequest qna -> toDocument(qna);
            case SingleChoiceQuizCreateRequest scq -> toDocument(scq);
            case MultipleChoiceQuizCreateRequest mcq -> toDocument(mcq);
        };
    }

    private QuestionAndAnswer toDocument(QuestionAndAnswerCreateRequest request) {
        return new QuestionAndAnswer(
                request.getTitle(),
                request.getTags(),
                request.getQuestion(),
                request.getAnswer()
        );
    }

    private SingleChoiceQuiz toDocument(SingleChoiceQuizCreateRequest request) {
        return new SingleChoiceQuiz(
                request.getTitle(),
                request.getTags(),
                request.getQuestion(),
                request.getOptions(),
                request.getCorrectOption()
        );
    }

    private MultipleChoiceQuiz toDocument(MultipleChoiceQuizCreateRequest request) {
        return new MultipleChoiceQuiz(
                request.getTitle(),
                request.getTags(),
                request.getQuestion(),
                request.getOptions(),
                request.getCorrectOptions()
        );
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
