package org.hyperskill.community.flashcards.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperskill.community.flashcards.category.model.Category;
import org.hyperskill.community.flashcards.common.ResourceNotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private static final String collection = "category";
    private static final int pageSize = 20;
    private final MongoTemplate mongoTemplate;

    public Page<Category> getCategories(String username, int page) {
        var usernameHasReadPermission = Criteria.where("username").is(username)
                .and("permission").regex("(.*r.*){1,3}");
        var currentUserHasAccess = Criteria.where("access").elemMatch(usernameHasReadPermission);

        var count = mongoTemplate.count(new Query(currentUserHasAccess), collection);

        var aggregation = Aggregation.newAggregation(
                Aggregation.match(currentUserHasAccess),
                Aggregation.sort(Sort.by("name")),
                Aggregation.skip((long) page * pageSize),
                Aggregation.limit(pageSize)
        );

        var categories = mongoTemplate.aggregate(aggregation, collection, Category.class).getMappedResults();

        return new PageImpl<>(categories, Pageable.ofSize(pageSize), count);
    }

    public Category findById(String username, String categoryId) {
        var category = Optional.ofNullable(mongoTemplate.findById(categoryId, Category.class))
                .orElseThrow(ResourceNotFound::new);

        var canAccess = category.access().stream().anyMatch(access -> access.username().equals(username));
        if (!canAccess) {
            throw new AccessDeniedException("Access denied");
        }
        return category;
    }
}
