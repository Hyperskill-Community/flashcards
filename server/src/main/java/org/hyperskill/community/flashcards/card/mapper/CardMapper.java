package org.hyperskill.community.flashcards.card.mapper;

import org.hyperskill.community.flashcards.card.model.Card;
import org.hyperskill.community.flashcards.card.model.MultipleChoiceQuiz;
import org.hyperskill.community.flashcards.card.model.QuestionAndAnswer;
import org.hyperskill.community.flashcards.card.model.SingleChoiceQuiz;
import org.hyperskill.community.flashcards.card.request.CardRequestDto;
import org.hyperskill.community.flashcards.card.request.MultipleChoiceQuizRequestDto;
import org.hyperskill.community.flashcards.card.request.QuestionAndAnswerRequestDto;
import org.hyperskill.community.flashcards.card.request.SingleChoiceQuizRequestDto;
import org.hyperskill.community.flashcards.card.response.CardItemProjection;
import org.hyperskill.community.flashcards.card.response.CardResponse;
import org.hyperskill.community.flashcards.card.response.CardType;
import org.hyperskill.community.flashcards.card.response.MultipleChoiceQuizDto;
import org.hyperskill.community.flashcards.card.response.QuestionAndAnswerDto;
import org.hyperskill.community.flashcards.card.response.SingleChoiceQuizDto;
import org.hyperskill.community.flashcards.common.ActionsParser;
import org.hyperskill.community.flashcards.common.response.PermittedAction;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CardMapper {

    public CardItemProjection map(Card card) {

        var type = switch (card) {
            case QuestionAndAnswer c -> CardType.QNA;
            case SingleChoiceQuiz c -> CardType.SCQ;
            case MultipleChoiceQuiz c -> CardType.MCQ;
        };
        return new CardItemProjection(card.getId(), card.getTitle(), type);
    }

    public CardResponse map(Card card, String categoryId) {
        var uri = "/api/cards/" + card.getId() + "?categoryId=" + categoryId;
        var actions = ActionsParser.fromPermissions(card.getPermissions(), uri);

        return switch (card) {
            case QuestionAndAnswer qna -> toDto(qna, actions);
            case SingleChoiceQuiz scq -> toDto(scq, actions);
            case MultipleChoiceQuiz mcq -> toDto(mcq, actions);
        };
    }

    public <T extends CardRequestDto> Card toDocument(T request) {
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

    private QuestionAndAnswerDto toDto(QuestionAndAnswer card, Set<PermittedAction> actions) {
        return QuestionAndAnswerDto.builder()
                .id(card.getId())
                .title(card.getTitle())
                .type("qna")
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .tags(card.getTags())
                .actions(actions)
                .createdAt(card.getCreatedAt())
                .build();
    }

    private SingleChoiceQuizDto toDto(SingleChoiceQuiz card, Set<PermittedAction> actions) {
        return SingleChoiceQuizDto.builder()
                .id(card.getId())
                .title(card.getTitle())
                .type("scq")
                .question(card.getQuestion())
                .options(card.getOptions())
                .correctOption(card.getCorrectOption())
                .tags(card.getTags())
                .actions(actions)
                .createdAt(card.getCreatedAt())
                .build();
    }

    private MultipleChoiceQuizDto toDto(MultipleChoiceQuiz card, Set<PermittedAction> actions) {
        return MultipleChoiceQuizDto.builder()
                .id(card.getId())
                .title(card.getTitle())
                .type("mcq")
                .question(card.getQuestion())
                .options(card.getOptions())
                .correctOptions(card.getCorrectOptions())
                .tags(card.getTags())
                .actions(actions)
                .createdAt(card.getCreatedAt())
                .build();
    }
}
