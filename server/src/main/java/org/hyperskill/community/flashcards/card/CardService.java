package org.hyperskill.community.flashcards.card;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperskill.community.flashcards.card.model.Card;
import org.hyperskill.community.flashcards.category.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardService {
    private static final int pageSize = 20;
    private final MongoTemplate mongoTemplate;
    private final CategoryService categoryService;

    public Page<Card> getCardsByCategory(String username, String categoryId, int page) {
        Objects.requireNonNull(categoryId, "Category ID cannot be null");

        var collection = categoryService.findById(username, categoryId).name();

        var aggregation = Aggregation.newAggregation(
                Aggregation.sort(Sort.by("name")),
                Aggregation.skip((long) page * pageSize),
                Aggregation.limit(pageSize)
        );

        var count = mongoTemplate.count(new Query(), collection);
        var cards = mongoTemplate.aggregate(aggregation, collection, Card.class).getMappedResults();

        return new PageImpl<>(cards, Pageable.ofSize(pageSize), count);
    }
}