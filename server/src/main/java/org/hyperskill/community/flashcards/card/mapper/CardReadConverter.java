package org.hyperskill.community.flashcards.card.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.hyperskill.community.flashcards.card.model.Card;
import org.hyperskill.community.flashcards.card.model.MultipleChoiceQuiz;
import org.hyperskill.community.flashcards.card.model.QuestionAndAnswerCard;
import org.hyperskill.community.flashcards.card.model.SingleChoiceQuiz;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
@RequiredArgsConstructor
public class CardReadConverter implements Converter<Document, Card> {
    private final ObjectMapper objectMapper;

    @Override
    public Card convert(Document source) {
        String typeAlias = source.getString("_class");
        Class<? extends Card> clazz = switch (typeAlias) {
            case "SCQ" -> SingleChoiceQuiz.class;
            case "MCQ" -> MultipleChoiceQuiz.class;
            case "QAC" -> QuestionAndAnswerCard.class;
            default -> throw new IllegalStateException("Unknown value " + typeAlias);
        };
        try {
            return objectMapper.readValue(source.toJson(), clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
