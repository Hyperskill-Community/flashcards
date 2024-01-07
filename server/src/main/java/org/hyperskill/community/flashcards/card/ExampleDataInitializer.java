package org.hyperskill.community.flashcards.card;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.hyperskill.community.flashcards.card.model.Card;
import org.hyperskill.community.flashcards.card.model.MultipleChoiceQuiz;
import org.hyperskill.community.flashcards.card.model.QuestionAndAnswerCard;
import org.hyperskill.community.flashcards.card.model.SingleChoiceQuiz;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Initializes the 'example' database with sample cards.
 */
@Component
@Slf4j
public class ExampleDataInitializer implements CommandLineRunner {
    private final MongoTemplate mongoTemplate;

    public ExampleDataInitializer(@Qualifier("example") MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void init() {
        List<Card> mathCards = List.of(
                QuestionAndAnswerCard.builder()
                        .author("test@test.com")
                        .title("card 1")
                        .isPublic(true)
                        .question("Calculate 2 + 2 = ?")
                        .answer("4")
                        .tags(Set.of("equations"))
                        .build(),
                SingleChoiceQuiz.builder()
                        .author("test@test.com")
                        .title("card 2")
                        .isPublic(true)
                        .question("Solve the equation: 2x + 3 = 9")
                        .options(List.of("2", "5", "6", "3"))
                        .correctOption(3)
                        .build(),
                MultipleChoiceQuiz.builder()
                        .author("test@test.com")
                        .title("card 3")
                        .isPublic(true)
                        .question("Select all correct statements about the equation: 2x + 3 = 9")
                        .options(List.of("It's a square equation", "It's a linear equation", "It doesn't have a root", "It's root is 3"))
                        .correctOptions(List.of(1, 3))
                        .build()
        );
        var geographyCards = List.of(
                QuestionAndAnswerCard.builder()
                        .author("test@test.com")
                        .title("card 4")
                        .isPublic(true)
                        .question("Capital of Spain")
                        .answer("Madrid")
                        .tags(Set.of("Europe", "cities"))
                        .build()
        );
        var chemistryCards = List.of(
                SingleChoiceQuiz.builder()
                        .author("test@test.com")
                        .title("card 5")
                        .isPublic(true)
                        .question("Which of the elements has the lowest atomic weight?")
                        .options(List.of("Au", "Fe", "Li", "Pt"))
                        .correctOption(2)
                        .tags(Set.of("elements", "atoms"))
                        .build()
        );

        log.info("Dropping any existing collections in 'example' database...");
        mongoTemplate.getDb().listCollectionNames().forEach(mongoTemplate::dropCollection);

        log.info("Inserting sample data to 'example' database...");
        mathCards.forEach(card -> mongoTemplate.insert(card, "Math"));
        geographyCards.forEach(card -> mongoTemplate.insert(card, "Geography"));
        chemistryCards.forEach(card -> mongoTemplate.insert(card, "Chemistry"));
    }

    public <T extends Card> List<T> getCardsByClass(Class<T> clazz, String collection) {
        var query = Query.query(Criteria.where("_class").is(clazz.getSimpleName()));
        return mongoTemplate.find(query, clazz, collection);
    }

    public List<Card> getAllCards(String collection) {
        return mongoTemplate.findAll(Card.class, collection);
    }

    @Override
    public void run(String... args) {
        log.info("Listing collections in 'example' database:");
        var collectionNames = mongoTemplate.getDb().listCollectionNames();
        for (var collectionName : collectionNames) {
            log.info("Collection: {}", collectionName);
            getAllCards(collectionName).stream()
                    .map(card -> switch (card) {
                        case SingleChoiceQuiz scq -> scq;
                        case MultipleChoiceQuiz mcq -> mcq;
                        case QuestionAndAnswerCard qac -> qac;
                    })
                    .forEach(System.out::println);
        }

        log.info("Listing cards by type in each collection in 'example' database:");
        for (var collectionName : collectionNames) {
            log.info("Collection: {}", collectionName);
            log.info("QnA cards:");
            getCardsByClass(QuestionAndAnswerCard.class, collectionName).forEach(System.out::println);
            log.info("SCQ cards:");
            getCardsByClass(SingleChoiceQuiz.class, collectionName).forEach(System.out::println);
            log.info("MCQ cards:");
            getCardsByClass(MultipleChoiceQuiz.class, collectionName).forEach(System.out::println);
        }
    }
}
