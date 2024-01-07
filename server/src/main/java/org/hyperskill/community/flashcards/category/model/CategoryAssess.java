package org.hyperskill.community.flashcards.category.model;

/**
 * Represents a set of permissions for a given user
 *
 * @param username   the name of the user
 * @param permission a string that defines the set of permissions.
 *                   The possible permissions are: <b>r</b> for read, <b>w</b> for write and <b>d</b> for delete
 */
public record CategoryAssess(String username, String permission) {
}
