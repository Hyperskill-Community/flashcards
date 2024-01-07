package org.hyperskill.community.flashcards.config;

import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@EnableMongoAuditing
public class MongoConfiguration {

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(MongoClients.create(), "cards");
    }

    @Bean
    @Qualifier("example")
    public MongoTemplate exampleMongoTemplate() {
        return new MongoTemplate(MongoClients.create(), "example");
    }
}
