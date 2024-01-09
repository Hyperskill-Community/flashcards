package org.hyperskill.community.flashcards.integration;

import org.hyperskill.community.flashcards.TestMongoConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import static org.assertj.core.api.Assertions.assertThat;

//@Disabled
@SpringBootTest(classes = TestMongoConfiguration.class)
class ExampleDataInitializerIT {

    // needed since otherwise test tries to connect to Authorization server on AppContext creation
    @MockBean
    ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @AfterEach
    void setup() {
        mongoTemplate.getDb().drop();
    }

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
