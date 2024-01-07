package org.hyperskill.community.flashcards.card;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.hyperskill.community.flashcards.card.model.Card;
import org.hyperskill.community.flashcards.card.model.MultipleChoiceQuiz;
import org.hyperskill.community.flashcards.card.model.QuestionAndAnswerCard;
import org.hyperskill.community.flashcards.card.model.SingleChoiceQuiz;
import org.hyperskill.community.flashcards.category.model.Category;
import org.hyperskill.community.flashcards.category.model.CategoryAccess;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Initializes the 'example' database with sample cards.
 */
@Component
@Slf4j
public class ExampleDataInitializer {
    private final MongoTemplate mongoTemplate;

    public ExampleDataInitializer(@Qualifier("example") MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void init() {
        List<Card> mathCards = List.of(
                QuestionAndAnswerCard.builder()
                        .title("card 1")
                        .question("Calculate 2 + 2 = ?")
                        .answer("4")
                        .tags(Set.of("equations"))
                        .build(),
                SingleChoiceQuiz.builder()
                        .title("card 2")
                        .question("Solve the equation: 2x + 3 = 9")
                        .options(List.of("2", "5", "6", "3"))
                        .correctOption(3)
                        .build(),
                MultipleChoiceQuiz.builder()
                        .title("card 3")
                        .question("Select all correct statements about the equation: 2x + 3 = 9")
                        .options(List.of("It's a square equation", "It's a linear equation", "It doesn't have a root", "It's root is 3"))
                        .correctOptions(List.of(1, 3))
                        .build()
        );
        var geographyCards = List.of(
                QuestionAndAnswerCard.builder()
                        .title("card 4")
                        .question("Capital of Spain")
                        .answer("Madrid")
                        .tags(Set.of("Europe", "cities"))
                        .build()
        );
        var chemistryCards = List.of(
                SingleChoiceQuiz.builder()
                        .title("card 5")
                        .question("Which of the elements has the lowest atomic weight?")
                        .options(List.of("Au", "Fe", "Li", "Pt"))
                        .correctOption(2)
                        .tags(Set.of("elements", "atoms"))
                        .build()
        );

        log.info("Dropping any existing collections in 'example' database...");
        mongoTemplate.getDb().listCollectionNames().forEach(mongoTemplate::dropCollection);

        log.info("Inserting sample data to 'example' database...");
        var categoryAccess = new CategoryAccess("test@test.com", "rwd");
        mongoTemplate.insert(new Category(null, "math", Set.of(categoryAccess)));
        mongoTemplate.insert(new Category(null, "geography", Set.of(categoryAccess)));
        mongoTemplate.insert(new Category(null, "chemistry", Set.of(categoryAccess)));

        mathCards.forEach(card -> mongoTemplate.insert(card, "math"));
        geographyCards.forEach(card -> mongoTemplate.insert(card, "geography"));
        chemistryCards.forEach(card -> mongoTemplate.insert(card, "chemistry"));
    }
}
