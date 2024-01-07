package org.hyperskill.community.flashcards.card;

import org.hyperskill.community.flashcards.TestMongoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestMongoConfiguration.class)
class ExampleDataInitializerIT {

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void whenNewContainerStarted_insertSampleData() {
        var db = mongoTemplate.getDb();
        var users = db.getCollection("user").countDocuments();
        assertThat(users).isEqualTo(2);

        var categories = db.getCollection("category").countDocuments();
        assertThat(categories).isEqualTo(1);

        var flashcards = db.getCollection("example").countDocuments();
        assertThat(flashcards).isEqualTo(12);
    }
}
