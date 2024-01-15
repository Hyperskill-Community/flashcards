package org.hyperskill.community.flashcards.card;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.hyperskill.community.flashcards.card.mapper.CardReadConverter;
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
    private static final int PAGE_SIZE = 20;
    private final MongoTemplate mongoTemplate;
    private final CategoryService categoryService;
    private final CardReadConverter converter;

    public Page<Card> getCardsByCategory(String username, String categoryId, int page) {
        final var collection = getCollectionName(username, categoryId);

        var aggregation = Aggregation.newAggregation(
                Aggregation.sort(Sort.by("name")),
                Aggregation.skip((long) page * PAGE_SIZE),
                Aggregation.limit(PAGE_SIZE)
        );

        var count = mongoTemplate.count(new Query(), collection);
        var result = mongoTemplate.aggregate(aggregation, collection, Document.class)
                .getRawResults()
                .getList("results", Document.class);
        var cards = result.stream().map(converter::convert).toList();
        return new PageImpl<>(cards, Pageable.ofSize(PAGE_SIZE), count);
    }

    private String getCollectionName(String username, String categoryId) {
        Objects.requireNonNull(categoryId, "Category ID cannot be null");

        return categoryService.findById(username, categoryId).name();
    }

    public long getCardCountOfCategory(String username, String categoryId) {
        final var collection = getCollectionName(username, categoryId);
        return mongoTemplate.count(new Query(), collection);
    }
}
