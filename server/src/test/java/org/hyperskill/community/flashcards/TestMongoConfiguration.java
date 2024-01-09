package org.hyperskill.community.flashcards;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MongoDBContainer;

import java.util.List;

@TestConfiguration(proxyBeanMethods = false)
public class TestMongoConfiguration {

    @Bean
    @ServiceConnection
    public MongoDBContainer mongoDBContainer() {
        var container = new MongoDBContainer("mongo:7.0.4-jammy").withReuse(true);
        container.setPortBindings(List.of("27017:27017"));
        return container;
    }
}
