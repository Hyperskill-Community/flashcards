package org.hyperskill.community.flashcards.card.mapper;

import org.hyperskill.community.flashcards.card.model.Card;
import org.hyperskill.community.flashcards.card.model.MultipleChoiceQuiz;
import org.hyperskill.community.flashcards.card.model.QuestionAndAnswer;
import org.hyperskill.community.flashcards.card.model.SingleChoiceQuiz;
import org.hyperskill.community.flashcards.card.request.CardRequest;
import org.hyperskill.community.flashcards.card.request.MultipleChoiceQuizRequestDto;
import org.hyperskill.community.flashcards.card.request.QuestionAndAnswerRequestDto;
import org.hyperskill.community.flashcards.card.request.SingleChoiceQuizRequestDto;
import org.hyperskill.community.flashcards.card.response.CardItemProjection;
import org.hyperskill.community.flashcards.card.response.CardResponse;
import org.hyperskill.community.flashcards.card.response.CardType;
import org.hyperskill.community.flashcards.card.response.MultipleChoiceQuizResponseDto;
import org.hyperskill.community.flashcards.card.response.QuestionAndAnswerResponseDto;
import org.hyperskill.community.flashcards.card.response.SingleChoiceQuizResponseDto;
import org.hyperskill.community.flashcards.common.ActionsParser;
import org.hyperskill.community.flashcards.common.response.PermittedAction;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CardMapper {

    public CardItemProjection map(Card card) {
        return new CardItemProjection(card.id(), card.question(), CardType.fromCard(card));
    }

    public CardResponse map(Card card, String categoryId) {
        var uri = "/api/cards/" + card.id() + "?categoryId=" + categoryId;
        var actions = ActionsParser.fromPermissions(card.permissions(), uri);

        return switch (card) {
            case QuestionAndAnswer qna -> toDto(qna, actions);
            case SingleChoiceQuiz scq -> toDto(scq, actions);
            case MultipleChoiceQuiz mcq -> toDto(mcq, actions);
        };
    }

    public <T extends CardRequest> Card toDocument(T request) {
        return switch (request) {
            case QuestionAndAnswerRequestDto qna -> toDocument(qna);
            case SingleChoiceQuizRequestDto scq -> toDocument(scq);
            case MultipleChoiceQuizRequestDto mcq -> toDocument(mcq);
        };
    }

    private QuestionAndAnswer toDocument(QuestionAndAnswerRequestDto request) {
        return QuestionAndAnswer.builder()
                .title(request.title())
                .tags(request.tags())
                .question(request.question())
                .answer(request.answer())
                .build();
    }

    private SingleChoiceQuiz toDocument(SingleChoiceQuizRequestDto request) {
        return SingleChoiceQuiz.builder()
                .title(request.title())
                .tags(request.tags())
                .question(request.question())
                .options(request.options())
                .correctOption(request.correctOption())
                .build();
    }

    private MultipleChoiceQuiz toDocument(MultipleChoiceQuizRequestDto request) {
        return MultipleChoiceQuiz.builder()
                .title(request.title())
                .tags(request.tags())
                .question(request.question())
                .options(request.options())
                .correctOptions(request.correctOptions())
                .build();
    }

    private QuestionAndAnswerResponseDto toDto(QuestionAndAnswer card, Set<PermittedAction> actions) {
        return QuestionAndAnswerResponseDto.builder()
                .id(card.id())
                .title(card.title())
                .type(CardType.QNA)
                .question(card.question())
                .answer(card.answer())
                .tags(card.tags())
                .actions(actions)
                .createdAt(card.createdAt())
                .build();
    }

    private SingleChoiceQuizResponseDto toDto(SingleChoiceQuiz card, Set<PermittedAction> actions) {
        return SingleChoiceQuizResponseDto.builder()
                .id(card.id())
                .title(card.title())
                .type(CardType.SCQ)
                .question(card.question())
                .options(card.options())
                .correctOption(card.correctOption())
                .tags(card.tags())
                .actions(actions)
                .createdAt(card.createdAt())
                .build();
    }

    private MultipleChoiceQuizResponseDto toDto(MultipleChoiceQuiz card, Set<PermittedAction> actions) {
        return MultipleChoiceQuizResponseDto.builder()
                .id(card.id())
                .title(card.title())
                .type(CardType.MCQ)
                .question(card.question())
                .options(card.options())
                .correctOptions(card.correctOptions())
                .tags(card.tags())
                .actions(actions)
                .createdAt(card.createdAt())
                .build();
    }
}
