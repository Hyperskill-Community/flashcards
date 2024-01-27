package org.hyperskill.community.flashcards.registration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void whenNewUser_thenUserHasNoRoles() {
        assertTrue(user.getAuthorities().isEmpty());
    }

    @Test
    void whenNewUser_thenUserIsActive() {
        assertTrue(user.isEnabled());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isAccountNonLocked());
    }
}
