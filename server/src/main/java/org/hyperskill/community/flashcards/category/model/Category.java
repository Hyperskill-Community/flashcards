package org.hyperskill.community.flashcards.category.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * Represents a collection of available card categories
 *
 * @param name   unique category name
 * @param description some descriptive text about the category
 * @param access access list that sets permissions for certain users.
 *               The creator of the category receives the full set of permissions,
 *               while other users can be granted permissions to view or modify
 *               the cards in the collection
 */
@Document
public record Category(
        @Id
        String id,
        String name,
        String description,
        Set<CategoryAccess> access
) {
}
