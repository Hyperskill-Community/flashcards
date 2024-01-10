package org.hyperskill.community.flashcards.card.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.Instant;

public class MongoDateConverter extends JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        JsonNode node = p.readValueAsTree();
        var date = node.get("$date").asText();
        return Instant.parse(date);
    }
}
