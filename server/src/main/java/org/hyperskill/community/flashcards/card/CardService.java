package org.hyperskill.community.flashcards.card;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.hyperskill.community.flashcards.card.mapper.CardMapper;
import org.hyperskill.community.flashcards.card.mapper.CardReadConverter;
import org.hyperskill.community.flashcards.card.model.Card;
import org.hyperskill.community.flashcards.card.request.CardCreateRequest;
import org.hyperskill.community.flashcards.category.CategoryService;
import org.hyperskill.community.flashcards.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardService {
    private static final int PAGE_SIZE = 20;
    private final MongoTemplate mongoTemplate;
    private final CategoryService categoryService;
    private final CardReadConverter converter;
    private final CardMapper mapper;

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

    public long getCardCountOfCategory(String username, String categoryId) {
        final var collection = getCollectionName(username, categoryId);
        return mongoTemplate.count(new Query(), collection);
    }

    public String createCard(String username, CardCreateRequest request, String categoryId) {
        var collection = getCollectionName(username, categoryId, "w");
        var card = mapper.toDocument(request);
        return mongoTemplate.insert(card, collection).getId();
    }

    public Card getCardById(String username, String cardId, String categoryId) {
        Objects.requireNonNull(cardId, "Card ID cannot be null");

        var collection = getCollectionName(username, categoryId);
        return Optional.ofNullable(mongoTemplate.findById(cardId, Document.class, collection))
                .map(converter::convert)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public void deleteCardById(String username, String cardId, String categoryId) {
        Objects.requireNonNull(cardId, "Card ID cannot be null");

        var collection = getCollectionName(username, categoryId, "d");
        var query = Query.query(Criteria.where("id").is(cardId));
        mongoTemplate.remove(query, collection);
    }

    private String getCollectionName(String username, String categoryId) {
        return getCollectionName(username, categoryId, "r");
    }

    /**
     * Gets a collection name if the provided user does have the specified permission. If the user
     * doesn't have the permission, <code>AccessDeniedException</code> is thrown.
     * @param username user's name
     * @param categoryId ID of the requested category
     * @param permission permission (r, w, or d),
     * @return category name
     */
    private String getCollectionName(String username, String categoryId, String permission) {
        Objects.requireNonNull(categoryId, "Category ID cannot be null");

        return categoryService.findById(username, categoryId, permission).name();
    }
}
