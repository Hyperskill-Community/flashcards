package org.hyperskill.community.flashcards.integration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyperskill.community.flashcards.TestMongoConfiguration;
import org.hyperskill.community.flashcards.card.CardService;
import org.hyperskill.community.flashcards.card.mapper.CardMapper;
import org.hyperskill.community.flashcards.card.model.Card;
import org.hyperskill.community.flashcards.card.model.QuestionAndAnswer;
import org.hyperskill.community.flashcards.card.request.CardRequest;
import org.hyperskill.community.flashcards.card.request.MultipleChoiceQuizRequestDto;
import org.hyperskill.community.flashcards.card.request.QuestionAndAnswerRequestDto;
import org.hyperskill.community.flashcards.card.request.SingleChoiceQuizRequestDto;
import org.hyperskill.community.flashcards.category.CategoryService;
import org.hyperskill.community.flashcards.category.model.Category;
import org.hyperskill.community.flashcards.category.model.CategoryAccess;
import org.hyperskill.community.flashcards.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@EnableMongoAuditing
@ContextConfiguration(classes = TestMongoConfiguration.class)
class CardServiceIT {

    private static final String PATH = "/json/cards.json";

    @Autowired
    MongoTemplate mongoTemplate;
    ObjectMapper objectMapper;
    CardService service;
    String categoryId;
    Card[] cards;
    HashMap<Integer, String> idMaps = new HashMap<>();

    @BeforeEach
    void setup() throws IOException {
        objectMapper = new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mongoTemplate.getDb().drop();
        service = new CardService(mongoTemplate, new CategoryService(mongoTemplate), new CardMapper());
        setupCategoryAndCards();
    }

    @Test
    void whenUserHasReadRight_findReturnsCard() {
        var card = service.getCardById("user1", idMaps.get(0), categoryId);
        var expected = cards[0].setPermissions("r");

        assertThat(card).usingRecursiveComparison()
            .ignoringFields("id", "createdAt")
            .isEqualTo(expected);
    }

    @Test
    void whenUserNoAccess_getCardThrows() {
        final var cardId = idMaps.get(0);
        assertThrows(AccessDeniedException.class,
                () -> service.getCardById("user4", cardId, categoryId));
    }

    @Test
    void whenUserHasReadRight_getCardsNoFilterReturnsAllSorted() {
        var response = service.getCardsByCategory("user1", categoryId, 0, null);
        var sortedCards = Arrays.stream(cards)
                .sorted(Comparator.comparing(Card::title))
                .map(card -> card.setPermissions("r"))
                .toList();

        assertThat(response.get())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "createdAt")
                .isEqualTo(sortedCards);
        assertEquals(cards.length, response.getTotalElements());
        assertEquals(0, response.getNumber());
        assertEquals(20, response.getSize());
        assertEquals(1, response.getTotalPages());
        assertTrue(response.isFirst());
        assertTrue(response.isLast());
    }

    @Test
    void whenUserHasReadRight_getCardsFilteredReturnsFiltered() {
        var response = service.getCardsByCategory("user1", categoryId, 0, "1");
        var sortedCards = Arrays.stream(cards)
                .sorted(Comparator.comparing(Card::title))
                .filter(card -> card.title().contains("1"))
                .map(card -> card.setPermissions("r"))
                .toList();
        assertThat(response.get())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "createdAt")
                .isEqualTo(sortedCards);
        assertEquals(sortedCards.size(), response.getTotalElements());
    }

    @Test
    void whenManyCards_pagingWorks() {
        for (int i = 0; i < 20; i++) {
            mongoTemplate.save(QuestionAndAnswer.builder().title("test" + i).build(), "cat");
        }
        // 20 new and 6 previous - visible for user1
        var page0 = service.getCardsByCategory("user1", categoryId, 0, null);
        var page1 = service.getCardsByCategory("user1", categoryId, 1, null);
        var page2 = service.getCardsByCategory("user1", categoryId, 2, null);
        assertEquals(26, page0.getTotalElements());
        assertEquals(2, page1.getTotalPages());
        assertEquals("test9", page1.getContent().getLast().title());
        assertTrue(page0.isFirst());
        assertTrue(page1.isLast());
        assertFalse(page0.isLast());
        assertFalse(page1.isFirst());
        assertTrue(page2.isEmpty());
    }

    @Test
    void whenGetCardCount_ResultCorrect() {
        assertEquals(cards.length, service.getCardCountOfCategory("user1", categoryId));
    }

    @Test
    void whenDeleteCardWithRights_CardDeleted() {
        var id = idMaps.get(0);
        assertEquals(1, service.deleteCardById("user3", id, categoryId));
        assertEquals(cards.length - 1, service.getCardCountOfCategory("user1", categoryId));
        assertThrows(ResourceNotFoundException.class, () -> service.getCardById("user1", id, categoryId));
    }

    @Test
    void whenDeleteCardNoRights_Denied() {
        var id = idMaps.get(0);
        assertThrows(AccessDeniedException.class, () -> service.deleteCardById("user2", id, categoryId));
    }

    static CardRequest[] whenUpdateCardWithRights_Updated() {
        return new CardRequest[]{
                QuestionAndAnswerRequestDto.builder()
                        .title("new").question("quest").tags(Set.of("tag")).build(),
                SingleChoiceQuizRequestDto.builder()
                        .title("new").question("quest").tags(Set.of("tag")).build(),
                MultipleChoiceQuizRequestDto.builder()
                        .title("new").question("quest").tags(Set.of("tag")).build(),
        };
    }

    @ParameterizedTest
    @MethodSource
    void whenUpdateCardWithRights_Updated(CardRequest request) {
        var id = idMaps.get(0);
        assertEquals("new", service.updateCardById("user2", id,
                request, categoryId).title());
        var updated = service.getCardById("user1", id, categoryId);
        assertEquals("new", updated.title());
        assertEquals("quest", updated.question());
        assertEquals(Set.of("tag"), updated.tags());
    }

    @Test
    void whenCreateCardWithRights_CreatedAndInsertedIntoCategory() {
        var request = QuestionAndAnswerRequestDto.builder()
                .title("new").question("quest").tags(Set.of("tag"))
                .build();
        var newId = service.createCard("user2", request, categoryId);
        var card = service.getCardById("user1", newId, categoryId);
        assertEquals("new", card.title());
        assertEquals("quest", card.question());
        assertEquals(Set.of("tag"), card.tags());
        assertEquals(cards.length + 1, service.getCardCountOfCategory("user1", categoryId));

    }

    @Test
    void whenCreateUpdateCardNoRights_Denied() {
        var id = idMaps.get(0);
        var request = QuestionAndAnswerRequestDto.builder().title("new").build();
        assertThrows(AccessDeniedException.class, () -> service.updateCardById("user1", id, request, categoryId));
        assertThrows(AccessDeniedException.class, () -> service.createCard("user1", request, categoryId));
    }

    private void setupCategoryAndCards() throws IOException {
        mongoTemplate.getCollection("category");
        addCategory("user1", "r", "user2", "rw", "user3", "rwd");
        cards = objectMapper.readValue(new ClassPathResource(PATH).getInputStream(), Card[].class);
        for (int i = 0; i < cards.length; i++) {
            var saved = mongoTemplate.save(cards[i], "cat");
            idMaps.put(i, saved.id());
        }
    }

    private void addCategory(String... access) {
        var categoryAccess = new HashSet<CategoryAccess>();
        for (var i = 0; i < access.length; i += 2) {
            categoryAccess.add(new CategoryAccess(access[i], access[i + 1]));
        }
        var saved = mongoTemplate.save(new Category(null, "cat", "descr", categoryAccess), "category");
        categoryId = saved.id();
        mongoTemplate.getDb().createCollection("cat");
    }

}
