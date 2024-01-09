package org.hyperskill.community.flashcards.card;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.hyperskill.community.flashcards.card.model.Card;
import org.hyperskill.community.flashcards.card.model.MultipleChoiceQuiz;
import org.hyperskill.community.flashcards.card.model.QuestionAndAnswerCard;
import org.hyperskill.community.flashcards.card.model.SingleChoiceQuiz;
import org.hyperskill.community.flashcards.category.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardService {
    private static final int pageSize = 20;
    private final MongoTemplate mongoTemplate;
    private final CategoryService categoryService;
    private final ObjectMapper objectMapper;

    public Page<Card> getCardsByCategory(String categoryId, int page) {
        Objects.requireNonNull(categoryId, "Category ID cannot be null");
        if (page < 0) {
            throw new IllegalArgumentException("Page number is out of bounds");
        }

        var collection = categoryService.findById(categoryId).name();

        var aggregation = Aggregation.newAggregation(
                Aggregation.sort(Sort.by("name")),
                Aggregation.skip((long) page * pageSize),
                Aggregation.limit(pageSize)
        );

        var count = mongoTemplate.count(new Query(), collection);
        var documents = mongoTemplate.aggregate(aggregation, collection, Document.class).getRawResults();

        try {
            var cards = mapDocumentsToCards(documents.getList("results", Document.class));
            return new PageImpl<>(cards, Pageable.ofSize(pageSize), count);
        } catch (JsonProcessingException e) {
            log.error("Error processing BSON document", e);
            throw new RuntimeException(e);
        }
    }

    private List<Card> mapDocumentsToCards(List<Document> documents) throws JsonProcessingException {
        List<Card> cards = new ArrayList<>(documents.size());
        for (var document : documents) {
            var clazz = switch (document.getString("_class")) {
                case "SingleChoiceQuiz" -> SingleChoiceQuiz.class;
                case "MultipleChoiceQuiz" -> MultipleChoiceQuiz.class;
                case "QuestionAndAnswerCard" -> QuestionAndAnswerCard.class;
                default -> throw new IllegalStateException("Unexpected value: " + document.getString("_class"));
            };
            var card = objectMapper.readValue(document.toJson(), clazz);
            cards.add(card);
        }
        return cards;
    }
}
