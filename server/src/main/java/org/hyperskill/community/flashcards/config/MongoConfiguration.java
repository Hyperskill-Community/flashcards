package org.hyperskill.community.flashcards.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.hyperskill.community.flashcards.card.mapper.CardReadConverter;
import org.hyperskill.community.flashcards.card.model.Card;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableMongoAuditing
public class MongoConfiguration {

    @Bean
    public MongoCustomConversions mongoCustomConversions(ObjectMapper objectMapper) {
        List<Converter<Document, Card>> converters = new ArrayList<>();
        converters.add(new CardReadConverter(objectMapper));
        return new MongoCustomConversions(converters);
    }
}
