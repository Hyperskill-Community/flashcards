package org.hyperskill.community.flashcards.card;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperskill.community.flashcards.card.model.Card;
import org.hyperskill.community.flashcards.card.model.MultipleChoiceQuiz;
import org.hyperskill.community.flashcards.card.model.QuestionAndAnswer;
import org.hyperskill.community.flashcards.card.model.SingleChoiceQuiz;
import org.hyperskill.community.flashcards.category.model.Category;
import org.hyperskill.community.flashcards.category.model.CategoryAccess;
import org.hyperskill.community.flashcards.registration.User;
import org.hyperskill.community.flashcards.registration.UserDto;
import org.hyperskill.community.flashcards.registration.UserMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Initializes the 'cards' database with sample cards.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ExampleDataInitializer {
    private static final String EXAMPLE = "example";
    private static final String USER = "user";
    private static final String CATEGORY = "category";
    public static final String ABORTING_DATABASE_INITIALIZATION = "Collection {} is not empty, aborting database initialization";
    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;
    private final MongoTemplate mongoTemplate;

    /**
     * This method checks if collections to be initialized are empty, and if yes
     * it reads data from <code>/resources/json</code> folder and insert it
     * in the said collections. This ensures that empty database is initialized
     * with the same data, while non-empty database state remains unchanged.
     */
    @PostConstruct
    public void init() {

        log.info("Inserting sample data to the database...");
        if (isCollectionNotEmpty(USER)) {
            log.warn(ABORTING_DATABASE_INITIALIZATION, USER);
            return;
        }
        if (isCollectionNotEmpty(CATEGORY)) {
            log.warn(ABORTING_DATABASE_INITIALIZATION, CATEGORY);
            return;
        }
        if (isCollectionNotEmpty(EXAMPLE)) {
            log.warn(ABORTING_DATABASE_INITIALIZATION, EXAMPLE);
            return;
        }

        Resource usersJson = new ClassPathResource("/json/users.json");
        Resource flashcardsJson = new ClassPathResource("/json/flashcards.json");

        try {
            List<UserDto> userDTOs = objectMapper.readValue(usersJson.getFile(), new TypeReference<>() {
            });
            List<User> users = userDTOs.stream().map(userMapper::toDocument).toList();
            if (users.isEmpty()) {
                throw new IllegalStateException("No users to insert");
            }
            mongoTemplate.insertAll(users);

            log.info("Sample users successfully inserted!");

            var categoryAccess = new CategoryAccess(users.getFirst().getUsername(), "rwd");
            mongoTemplate.insert(new Category(null, EXAMPLE,
                    "This is a sample card set", Set.of(categoryAccess)));

            JsonNode jsonNode = objectMapper.readTree(flashcardsJson.getFile());

            for (int i = 0; i < 5; i++) {
                List<SingleChoiceQuiz> scqCards = parseCards(jsonNode.get("scq_cards"), SingleChoiceQuiz.class);
                List<MultipleChoiceQuiz> mcqCards = parseCards(jsonNode.get("mcq_cards"), MultipleChoiceQuiz.class);
                List<QuestionAndAnswer> qnaCards = parseCards(jsonNode.get("qna_cards"), QuestionAndAnswer.class);

                mongoTemplate.insert(scqCards, EXAMPLE);
                mongoTemplate.insert(mcqCards, EXAMPLE);
                mongoTemplate.insert(qnaCards, EXAMPLE);
            }
            log.info("Sample flashcards successfully inserted!");

        } catch (Exception e) {
            log.error("Error updating database", e);
            mongoTemplate.getDb().drop();
        }
    }

    private <T extends Card> List<T> parseCards(JsonNode jsonNode, Class<T> clazz) throws JsonProcessingException {
        List<T> list = new ArrayList<>();
        for (JsonNode node : jsonNode) {
            T card = objectMapper.treeToValue(node, clazz);
            list.add(card);
        }
        return list;
    }

    private boolean isCollectionNotEmpty(String collection) {
        var collectionExists = mongoTemplate.getDb().listCollectionNames()
                .into(new ArrayList<>())
                .contains(collection);

        return collectionExists && mongoTemplate.getDb().getCollection(collection).countDocuments() > 0;
    }
}
