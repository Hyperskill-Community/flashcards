package org.hyperskill.community.flashcards.category;

import com.mongodb.BasicDBObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperskill.community.flashcards.category.model.Category;
import org.hyperskill.community.flashcards.category.model.CategoryAccess;
import org.hyperskill.community.flashcards.category.request.CategoryCreateRequest;
import org.hyperskill.community.flashcards.category.request.CategoryUpdateRequest;
import org.hyperskill.community.flashcards.common.exception.ResourceAlreadyExists;
import org.hyperskill.community.flashcards.common.exception.ResourceNotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.OutOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.query.Update.update;

import java.util.Optional;
import java.util.Set;

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

    public String createCategory(String username, CategoryCreateRequest request) {
        // we assume that category names are unique which is still subject to discussion
        var categoryName = request.name();

        var query = Query.query(Criteria.where("name").is(categoryName));
        var category = mongoTemplate.findOne(query, Category.class, "category");
        if (category != null) {
            throw new ResourceAlreadyExists();
        }

        var access = new CategoryAccess(username, "rwd");
        var newCategory = new Category(null, categoryName, Set.of(access));
        newCategory = mongoTemplate.insert(newCategory, "category");
        mongoTemplate.getDb().createCollection(categoryName);
        return newCategory.id();
    }

    public void deleteById(String username, String categoryId) {
        var category = findById(username, categoryId);
        mongoTemplate.remove(category, "category");
        mongoTemplate.dropCollection(category.name());
    }

    public Category updateById(String username, String categoryId, CategoryUpdateRequest request) {
        // find if the requested collection exists and can be modified
        var category = findById(username, categoryId);

        // copy all documents from the old collection to the new collection
        var outOperation = new OutOperation(request.name());
        var aggregation = Aggregation.newAggregation(outOperation);
        mongoTemplate.aggregate(aggregation, category.name(), BasicDBObject.class);

        // delete old collection
        mongoTemplate.dropCollection(category.name());

        // update document in 'category' collection
        var query = Query.query(Criteria.where("id").is(categoryId));
        mongoTemplate.updateFirst(query, update("name", request.name()), Category.class);

        // return the updated document from 'category' collection
        return mongoTemplate.findOne(query, Category.class);
    }
}
