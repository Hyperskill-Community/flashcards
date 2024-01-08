package org.hyperskill.community.authserver.config;

import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Slf4j
public class MongoConfiguration {

    @Bean
    public MongoTemplate mongoTemplate(Environment env) {
        var connectionString = "mongodb://%s:27017".formatted(env.getProperty("MONGO_HOST"));
        log.info("Connecting to MongoDB at {}", connectionString);
        return new MongoTemplate(MongoClients.create(connectionString), "cards");
    }
}
