package org.hyperskill.community.flashcards.card;

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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardService {
    private static final int pageSize = 20;
    private final MongoTemplate mongoTemplate;
    private final CategoryService categoryService;

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
        var cards = mapDocumentToCard(documents.getList("results", Document.class));
        return new PageImpl<>(cards, Pageable.ofSize(pageSize), count);
    }

    private List<Card> mapDocumentToCard(List<Document> documents) {
        List<Card> cards = new ArrayList<>(documents.size());
        for (var document : documents) {
            var card = switch (document.getString("_class")) {
                case "SingleChoiceQuiz" -> {
                    var scq = new SingleChoiceQuiz();
                    scq.setId(document.getObjectId("_id").toHexString());
                    scq.setTitle(document.getString("title"));
                    scq.setQuestion(document.getString("question"));
                    scq.setOptions(document.getList("options", String.class));
                    scq.setCorrectOption(document.getInteger("correctOption"));
                    scq.setCreatedAt(document.getDate("createdAt").toInstant());
                    scq.setTags(new HashSet<>(document.getList("tags", String.class)));
                    yield scq;
                }
                case "MultipleChoiceQuiz" -> {
                    var mcq = new MultipleChoiceQuiz();
                    mcq.setId(document.getObjectId("_id").toHexString());
                    mcq.setTitle(document.getString("title"));
                    mcq.setQuestion(document.getString("question"));
                    mcq.setOptions(document.getList("options", String.class));
                    mcq.setCorrectOptions(document.getList("correctOptions", Integer.class));
                    mcq.setCreatedAt(document.getDate("createdAt").toInstant());
                    mcq.setTags(new HashSet<>(document.getList("tags", String.class)));
                    yield mcq;
                }
                case "QuestionAndAnswerCard" -> {
                    var qna = new QuestionAndAnswerCard();
                    qna.setId(document.getObjectId("_id").toHexString());
                    qna.setTitle(document.getString("title"));
                    qna.setQuestion(document.getString("question"));
                    qna.setAnswer(document.getString("answer"));
                    qna.setCreatedAt(document.getDate("createdAt").toInstant());
                    qna.setTags(new HashSet<>(document.getList("tags", String.class)));
                    yield qna;
                }
                default -> throw new IllegalStateException("Unexpected value: " + document.getString("_class"));
            };
            cards.add(card);
        }
        return cards;
    }
}
