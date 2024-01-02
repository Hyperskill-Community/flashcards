package org.hyperskill.community.flashcards.web;

import org.hyperskill.community.flashcards.registration.UserDto;
import org.hyperskill.community.flashcards.registration.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserMapperTest {
    UserDto dto;
    UserMapper mapper;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        passwordEncoder = new BCryptPasswordEncoder();
        mapper = new UserMapper(passwordEncoder);
        dto = new UserDto("a@b.c", "secret");
    }

    @Test
    void toDocument() {
        var mapped = mapper.toDocument(dto);
        assertEquals(dto.email(), mapped.getUsername());
        assertTrue(passwordEncoder.matches(dto.password(), mapped.getPassword()));
    }
}