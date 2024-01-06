package org.hyperskill.community.flashcards;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MongoDBContainer;

import java.util.List;

@TestConfiguration
public class TestMongoConfiguration {

    @Bean
    @ServiceConnection
    public MongoDBContainer mongoDBContainer() {
        var container = new MongoDBContainer("mongo:latest");
        container.setPortBindings(List.of("27017:27017"));
        return container;
    }
}
