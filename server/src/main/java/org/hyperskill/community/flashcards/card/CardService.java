package org.hyperskill.community.flashcards.card;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.hyperskill.community.flashcards.card.mapper.CardMapper;
import org.hyperskill.community.flashcards.card.mapper.CardReadConverter;
import org.hyperskill.community.flashcards.card.model.Card;
import org.hyperskill.community.flashcards.card.request.BaseCardUpdateRequest;
import org.hyperskill.community.flashcards.card.request.CardCreateRequest;
import org.hyperskill.community.flashcards.card.request.CardUpdateRequest;
import org.hyperskill.community.flashcards.card.request.MultipleChoiceQuizUpdateRequest;
import org.hyperskill.community.flashcards.card.request.QuestionAndAnswerUpdateRequest;
import org.hyperskill.community.flashcards.card.request.SingleChoiceQuizUpdateRequest;
import org.hyperskill.community.flashcards.category.CategoryService;
import org.hyperskill.community.flashcards.category.model.Category;
import org.hyperskill.community.flashcards.category.model.CategoryAccess;
import org.hyperskill.community.flashcards.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
        Objects.requireNonNull(categoryId, "Category ID cannot be null");

        final var category = categoryService.findById(username, categoryId);

        var aggregation = Aggregation.newAggregation(
                Aggregation.sort(Sort.by("name")),
                Aggregation.skip((long) page * PAGE_SIZE),
                Aggregation.limit(PAGE_SIZE)
        );

        var count = mongoTemplate.count(new Query(), category.name());
        var result = mongoTemplate.aggregate(aggregation, category.name(), Document.class)
                .getRawResults()
                .getList("results", Document.class);
        var cards = result.stream().map(converter::convert).toList();

        var permissions = getPermissions(username, category);
        cards.forEach(card -> card.setPermissions(permissions));

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
        Objects.requireNonNull(categoryId, "Category ID cannot be null");

        var category = categoryService.findById(username, categoryId);

        var card = Optional.ofNullable(mongoTemplate.findById(cardId, Document.class, category.name()))
                .map(converter::convert)
                .orElseThrow(ResourceNotFoundException::new);

        var permissions = getPermissions(username, category);
        card.setPermissions(permissions);

        return card;
    }

    public void deleteCardById(String username, String cardId, String categoryId) {
        Objects.requireNonNull(cardId, "Card ID cannot be null");

        var collection = getCollectionName(username, categoryId, "d");
        var query = Query.query(Criteria.where("id").is(cardId));
        mongoTemplate.remove(query, collection);
    }

    public Card updateCardById(String username, String cardId, CardUpdateRequest request, String categoryId) {
        Objects.requireNonNull(cardId, "Card ID cannot be null");
        Objects.requireNonNull(categoryId, "Category ID cannot be null");

        var category = categoryService.findById(username, categoryId, "w");

        var query = Query.query(Criteria.where("_id").is(cardId));
        mongoTemplate.updateFirst(query, updateFrom(request), category.name());

        var updatedCard = Optional.ofNullable(mongoTemplate.findOne(query, Document.class, category.name()))
                .map(converter::convert)
                .orElseThrow(ResourceNotFoundException::new);

        var permissions = getPermissions(username, category);
        updatedCard.setPermissions(permissions);

        return updatedCard;
    }

    private String getCollectionName(String username, String categoryId) {
        return getCollectionName(username, categoryId, "r");
    }

    /**
     * Gets a collection name if the provided user does have the specified permission. If the user
     * doesn't have the permission, <code>AccessDeniedException</code> is thrown.
     *
     * @param username   user's name
     * @param categoryId ID of the requested category
     * @param permission permission (r, w, or d),
     * @return category name
     */
    private String getCollectionName(String username, String categoryId, String permission) {
        Objects.requireNonNull(categoryId, "Category ID cannot be null");

        return categoryService.findById(username, categoryId, permission).name();
    }

    private Update updateFrom(CardUpdateRequest request) {
        Objects.requireNonNull(request, "Update request cannot be null");

        var baseUpdateRequest = (BaseCardUpdateRequest) request;
        var update = new Update()
                .set("title", baseUpdateRequest.getTitle())
                .set("question", baseUpdateRequest.getQuestion())
                .set("tags", baseUpdateRequest.getTags());

        return switch (request) {
            case QuestionAndAnswerUpdateRequest qna -> update.set("answer", qna.getAnswer());
            case SingleChoiceQuizUpdateRequest scq -> update.set("options", scq.getOptions())
                    .set("correctOption", scq.getCorrectOption());
            case MultipleChoiceQuizUpdateRequest mcq -> update.set("options", mcq.getOptions())
                    .set("correctOptions", mcq.getCorrectOptions());
        };
    }

    private String getPermissions(String username, Category category) {
        return category.access().stream()
                .filter(categoryAccess -> categoryAccess.username().equals(username))
                .map(CategoryAccess::permission)
                .findFirst().orElseThrow(() -> {
                    var message = "Category %s doesn't have access rules for user %s".formatted(category.id(), username);
                    return new IllegalStateException(message);
                });
    }
}
