package org.hyperskill.community.flashcards.category;

import com.mongodb.MongoNamespace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperskill.community.flashcards.category.model.Category;
import org.hyperskill.community.flashcards.category.model.CategoryAccess;
import org.hyperskill.community.flashcards.category.request.CategoryCreateRequest;
import org.hyperskill.community.flashcards.category.request.CategoryUpdateRequest;
import org.hyperskill.community.flashcards.common.exception.ResourceAlreadyExistsException;
import org.hyperskill.community.flashcards.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Set;

import static org.springframework.data.mongodb.core.query.Update.update;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private static final String CATEGORY = "category";
    private static final int PAGE_SIZE = 20;

    private final MongoTemplate mongoTemplate;

    public Page<Category> getCategories(String username, int page) {
        var usernameHasReadPermission = Criteria.where("username").is(username)
                .and("permission").regex(".*r.*");
        var categoriesQuery = new Query(Criteria.where("access").elemMatch(usernameHasReadPermission));

        var count = mongoTemplate.count(categoriesQuery, CATEGORY);
        var pageRequest = PageRequest.of(page, PAGE_SIZE, Sort.by("name"));
        var categories = mongoTemplate.find(categoriesQuery.with(pageRequest), Category.class, CATEGORY);

        return new PageImpl<>(categories, pageRequest, count);
    }

    public Set<Category> getCategories(String username) {
        var usernameHasReadPermission = Criteria.where("username").is(username)
                .and("permission").regex(".*r.*");
        var categoriesQuery = new Query(Criteria.where("access").elemMatch(usernameHasReadPermission));
        return Set.copyOf(mongoTemplate.find(categoriesQuery, Category.class, CATEGORY));
    }

    public Category findById(String username, String categoryId) {
        return findById(username, categoryId, "r");
    }

    public Category findById(String username, String categoryId, String permission) {
        var category = Optional.ofNullable(mongoTemplate.findById(categoryId, Category.class))
                .orElseThrow(ResourceNotFoundException::new);
        assertCanAccess(username, category, permission);

        return category;
    }

    public String createCategory(String username, CategoryCreateRequest request) {
        // we assume that category names are unique which is still subject to discussion
        var categoryName = request.name();
        throwIfCategoryExists(categoryName);

        var access = new CategoryAccess(username, "rwd");
        var newCategory = new Category(null, categoryName, request.description(), Set.of(access));
        newCategory = mongoTemplate.insert(newCategory, CATEGORY);
        mongoTemplate.getDb().createCollection(categoryName);

        return newCategory.id();
    }

    public void deleteById(String username, String categoryId) {
        var category = findById(username, categoryId, "d");
        mongoTemplate.remove(category, CATEGORY);
        mongoTemplate.dropCollection(category.name());
    }

    public Category updateById(String username, String categoryId, CategoryUpdateRequest request) {
        // find if the requested collection exists and can be modified
        var category = findById(username, categoryId, "w");
        renameCategoryIfNameChanged(category, request);
        return updateCategoryDocument(categoryId, request);
    }

    private Category updateCategoryDocument(String categoryId, CategoryUpdateRequest request) {
        var query = Query.query(Criteria.where("id").is(categoryId));
        var update = update("description", request.description());
        // update name only if it's not blank in the request
        if (StringUtils.hasText(request.name())) {
            update.set("name", request.name());
        }

        mongoTemplate.updateFirst(query, update, Category.class);
        return mongoTemplate.findOne(query, Category.class);
    }

    private void renameCategoryIfNameChanged(Category category, CategoryUpdateRequest request) {
        if (category.name().equals(request.name()) || !StringUtils.hasText(request.name())) {
            return;
        }
        // check if the new name is already taken
        throwIfCategoryExists(request.name());
        // rename the existing collection
        var namespace = new MongoNamespace(mongoTemplate.getDb().getName(), request.name());
        mongoTemplate.getCollection(category.name()).renameCollection(namespace);
    }

    private void throwIfCategoryExists(String name) {
        if (mongoTemplate.getCollectionNames().contains(name)) {
            throw new ResourceAlreadyExistsException();
        }
    }

    private void assertCanAccess(String username, Category category, String permission) {
        if (category.access().stream()
                .noneMatch(access -> access.username().equals(username)
                                     && access.permission().contains(permission))) {
            throw new AccessDeniedException("Access '%s' denied".formatted(permission));
        }
    }
}
