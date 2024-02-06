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
import org.hyperskill.community.flashcards.common.response.ActionType;
import org.hyperskill.community.flashcards.common.response.PermittedAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardMapperTest {

    CardMapper cardMapper;

    static final String CATEGORY_ID = "3456";
    static final String URI = "/api/cards/%s?categoryId=" + CATEGORY_ID;

    static final List<Card> CARDS = List.of(
            QuestionAndAnswer.builder()
                    .id("1")
                    .title("title")
                    .tags(Set.of("tag1", "tag2"))
                    .question("question")
                    .answer("answer")
                    .permissions("rwd")
                    .build(),
            SingleChoiceQuiz.builder()
                    .id("2")
                    .title("another title")
                    .question("question2")
                    .tags(Set.of("tag3", "tag4"))
                    .options(List.of("option1", "option2"))
                    .permissions("rw")
                    .correctOption(1)
                    .build(),
            MultipleChoiceQuiz.builder()
                    .id("12345")
                    .title("multi")
                    .question("question3")
                    .tags(Set.of("tag5", "tag6"))
                    .permissions("r")
                    .options(List.of("option1", "option2"))
                    .correctOptions(List.of(1, 2))
                    .build()
    );

    static final List<CardRequest> REQUESTS = List.of(
            new QuestionAndAnswerRequestDto("title",
                    Set.of("tag1", "tag2"), "question", "answer"),
            new SingleChoiceQuizRequestDto(
                    "another title", Set.of("tag3", "tag4"), "question2",
                    List.of("option1", "option2"), 1),
            new MultipleChoiceQuizRequestDto(
                    "multi", Set.of("tag5", "tag6"), "question3",
                    List.of("option1", "option2"), List.of(1, 2))
    );

    static final List<CardResponse> RESPONSES = List.of(
            new QuestionAndAnswerResponseDto("1", "title", "question",
                    Set.of("tag1", "tag2"), CardType.QNA, null,
                    Set.of(new PermittedAction(ActionType.READ, URI.formatted("1")),
                            new PermittedAction(ActionType.WRITE, URI.formatted("1")),
                            new PermittedAction(ActionType.DELETE, URI.formatted("1"))), "answer"),
            new SingleChoiceQuizResponseDto("2",
                    "another title", "question2", Set.of("tag3", "tag4"), CardType.SCQ, null,
                    Set.of(new PermittedAction(ActionType.READ, URI.formatted("2")),
                            new PermittedAction(ActionType.WRITE, URI.formatted("2"))),
                            List.of("option1", "option2"), 1),
            new MultipleChoiceQuizResponseDto("12345",
                    "multi", "question3", Set.of("tag5", "tag6"), CardType.MCQ, null,
                    Set.of(new PermittedAction(ActionType.READ, URI.formatted("12345"))),
                    List.of("option1", "option2"), List.of(1, 2))
    );

    @BeforeEach
    void setUp() {
        cardMapper = new CardMapper();
    }

    static Stream<Arguments> cardItemProvider() {
        return Stream.of(
                Arguments.of(CARDS.getFirst(), new CardItemProjection("1", "title", CardType.QNA)),
                Arguments.of(CARDS.get(1), new CardItemProjection("2", "another title", CardType.SCQ)),
                Arguments.of(CARDS.getLast(), new CardItemProjection("12345", "multi", CardType.MCQ))
        );
    }

    @ParameterizedTest
    @MethodSource("cardItemProvider")
    void cardToCardItemProjection(Card card, CardItemProjection expected) {
        assertEquals(expected, cardMapper.map(card));
    }

    static Stream<Arguments> cardRequestProvider() {
        return Stream.of(
                Arguments.of(REQUESTS.getFirst(), CARDS.getFirst()),
                Arguments.of(REQUESTS.get(1), CARDS.get(1)),
                Arguments.of(REQUESTS.getLast(), CARDS.getLast())
        );
    }

    @ParameterizedTest
    @MethodSource("cardRequestProvider")
    void cardRequestToCard(CardRequest request, Card card) {
        var mapped = cardMapper.toDocument(request);
        assertEquals(card.getTitle(), mapped.getTitle());
        assertEquals(card.getTags(), mapped.getTags());
        assertEquals(card.getQuestion(), mapped.getQuestion());
        switch (card) {
            case QuestionAndAnswer qna ->
                    assertEquals(qna.getAnswer(), ((QuestionAndAnswer) mapped).getAnswer());
            case SingleChoiceQuiz scq -> {
                assertEquals(scq.getOptions(), ((SingleChoiceQuiz) mapped).getOptions());
                assertEquals(scq.getCorrectOption(), ((SingleChoiceQuiz) mapped).getCorrectOption());
            }
            case MultipleChoiceQuiz mcq -> {
                assertEquals(mcq.getOptions(), ((MultipleChoiceQuiz) mapped).getOptions());
                assertEquals(mcq.getCorrectOptions(), ((MultipleChoiceQuiz) mapped).getCorrectOptions());
            }
        }
    }

    static Stream<Arguments> cardResponseProvider() {
        return Stream.of(
                Arguments.of(RESPONSES.getFirst(), CARDS.getFirst()),
                Arguments.of(RESPONSES.get(1), CARDS.get(1)),
                Arguments.of(RESPONSES.getLast(), CARDS.getLast())
        );
    }

    @ParameterizedTest
    @MethodSource("cardResponseProvider")
    void cardToCardResonse(CardResponse response, Card card) {
        var mapped = cardMapper.map(card, CATEGORY_ID);
        assertEquals(response.id(), mapped.id());
        assertEquals(response.title(), mapped.title());
        assertEquals(response.tags(), mapped.tags());
        assertEquals(response.type(), mapped.type());
        assertEquals(response.question(), mapped.question());
        assertEquals(response.actions(), mapped.actions());
        switch (response) {
            case QuestionAndAnswerResponseDto qna ->
                    assertEquals(qna.answer(), ((QuestionAndAnswerResponseDto) mapped).answer());
            case SingleChoiceQuizResponseDto scq -> {
                assertEquals(scq.options(), ((SingleChoiceQuizResponseDto) mapped).options());
                assertEquals(scq.correctOption(), ((SingleChoiceQuizResponseDto) mapped).correctOption());
            }
            case MultipleChoiceQuizResponseDto mcq -> {
                assertEquals(mcq.options(), ((MultipleChoiceQuizResponseDto) mapped).options());
                assertEquals(mcq.correctOptions(), ((MultipleChoiceQuizResponseDto) mapped).correctOptions());
            }
        }
    }
}
