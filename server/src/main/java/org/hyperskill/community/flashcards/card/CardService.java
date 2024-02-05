package org.hyperskill.community.flashcards.card;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperskill.community.flashcards.card.mapper.CardMapper;
import org.hyperskill.community.flashcards.card.model.Card;
import org.hyperskill.community.flashcards.card.request.CardRequest;
import org.hyperskill.community.flashcards.card.request.MultipleChoiceQuizRequestDto;
import org.hyperskill.community.flashcards.card.request.QuestionAndAnswerRequestDto;
import org.hyperskill.community.flashcards.card.request.SingleChoiceQuizRequestDto;
import org.hyperskill.community.flashcards.category.CategoryService;
import org.hyperskill.community.flashcards.category.model.Category;
import org.hyperskill.community.flashcards.category.model.CategoryAccess;
import org.hyperskill.community.flashcards.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
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
    private final CardMapper mapper;

    public Page<Card> getCardsByCategory(String username, String categoryId, int page, String titleFilter) {
        Objects.requireNonNull(categoryId, "Category ID cannot be null");

        final var category = categoryService.findById(username, categoryId);
        var pageRequest = PageRequest.of(page, PAGE_SIZE, Sort.by("title"));
        var query = createFilterQuery(titleFilter);
        var count = mongoTemplate.count(query, category.name());
        var cards = mongoTemplate.find(query.with(pageRequest), Card.class, category.name());

        var permissions = getPermissions(username, category);
        cards.forEach(card -> card.setPermissions(permissions));
        return new PageImpl<>(cards, pageRequest, count);
    }

    private Query createFilterQuery(String titleFilter) {
        var query = new Query();
        if (Objects.nonNull(titleFilter)) {
            var criteria = Criteria.where("title").regex(".*" + titleFilter + ".*", "i");
            query.addCriteria(criteria);
        }
        return query;
    }

    public long getCardCountOfCategory(String username, String categoryId) {
        final var collection = getCollectionName(username, categoryId);
        return mongoTemplate.count(new Query(), collection);
    }

    public String createCard(String username, CardRequest request, String categoryId) {
        var collection = getCollectionName(username, categoryId, "w");
        var card = mapper.toDocument(request);
        return mongoTemplate.insert(card, collection).getId();
    }

    public Card getCardById(String username, String cardId, String categoryId) {
        Objects.requireNonNull(cardId, "Card ID cannot be null");
        Objects.requireNonNull(categoryId, "Category ID cannot be null");

        var category = categoryService.findById(username, categoryId);
        var card = Optional.ofNullable(mongoTemplate.findById(cardId, Card.class, category.name()))
                .orElseThrow(ResourceNotFoundException::new);
        card.setPermissions(getPermissions(username, category));
        return card;
    }

    public void deleteCardById(String username, String cardId, String categoryId) {
        Objects.requireNonNull(cardId, "Card ID cannot be null");

        var collection = getCollectionName(username, categoryId, "d");
        var query = Query.query(Criteria.where("id").is(cardId));
        mongoTemplate.remove(query, collection);
    }

    public Card updateCardById(String username, String cardId, CardRequest request, String categoryId) {
        Objects.requireNonNull(cardId, "Card ID cannot be null");
        Objects.requireNonNull(categoryId, "Category ID cannot be null");

        var category = categoryService.findById(username, categoryId, "w");
        var query = Query.query(Criteria.where("_id").is(cardId));
        mongoTemplate.updateFirst(query, updateFrom(request), category.name());

        var updatedCard = Optional.ofNullable(mongoTemplate.findOne(query, Card.class, category.name()))
                .orElseThrow(ResourceNotFoundException::new);
        updatedCard.setPermissions(getPermissions(username, category));
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

    private Update updateFrom(CardRequest request) {
        Objects.requireNonNull(request, "Update request cannot be null");

        var update = new Update()
                .set("title", request.title())
                .set("question", request.question())
                .set("tags", request.tags());

        return switch (request) {
            case QuestionAndAnswerRequestDto qna -> update.set("answer", qna.answer());
            case SingleChoiceQuizRequestDto scq -> update.set("options", scq.options())
                    .set("correctOption", scq.correctOption());
            case MultipleChoiceQuizRequestDto mcq -> update.set("options", mcq.options())
                    .set("correctOptions", mcq.correctOptions());
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
