package org.hyperskill.community.flashcards.card;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.ListCollectionNamesIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.hyperskill.community.flashcards.registration.User;
import org.hyperskill.community.flashcards.registration.UserDto;
import org.hyperskill.community.flashcards.registration.UserMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class ExampleDataInitializerTest {
    @Mock
    ObjectMapper objectMapper;

    @Mock
    MongoTemplate mongoTemplate;

    @Mock
    MongoDatabase mongoDatabase;

    @Mock
    ListCollectionNamesIterable mongoIterable;

    @Mock
    MongoCollection<Document> mongoCollection;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    ExampleDataInitializer dataInitializer;

    @Test
    void whenUserCollectionNotEmpty_DoNothing() {
        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);
        when(mongoDatabase.listCollectionNames()).thenReturn(mongoIterable);
        when(mongoIterable.into(any())).thenReturn(new ArrayList<>(List.of("user")));
        when(mongoDatabase.getCollection("user")).thenReturn(mongoCollection);
        when(mongoCollection.countDocuments()).thenReturn(1L);

        dataInitializer.init();

        verify(mongoTemplate, never()).insertAll(any());
        verify(mongoTemplate, never()).insert(any());
        verify(userMapper, never()).toDocument(any());
    }

    @Test
    void whenCategoryCollectionNotEmpty_DoNothing() {
        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);
        when(mongoDatabase.listCollectionNames()).thenReturn(mongoIterable);
        when(mongoIterable.into(any())).thenReturn(new ArrayList<>(List.of("category")));
        when(mongoDatabase.getCollection("category")).thenReturn(mongoCollection);
        when(mongoCollection.countDocuments()).thenReturn(1L);

        dataInitializer.init();

        verify(mongoTemplate, never()).insertAll(any());
        verify(mongoTemplate, never()).insert(any());
    }

    @Test
    void whenExampleCollectionNotEmpty_DoNothing() {
        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);
        when(mongoDatabase.listCollectionNames()).thenReturn(mongoIterable);
        when(mongoIterable.into(any())).thenReturn(new ArrayList<>(List.of("example")));
        when(mongoDatabase.getCollection("example")).thenReturn(mongoCollection);
        when(mongoCollection.countDocuments()).thenReturn(1L);

        dataInitializer.init();

        verify(mongoTemplate, never()).insertAll(any());
        verify(mongoTemplate, never()).insert(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void whenCollectionsEmpty_insertData() throws IOException {
        var userJson = """
                [
                  {
                    "email": "test1@test.com",
                    "password": "12345678"
                  }
                ]
                """;
        var flashcardJson = """
                {
                  "qna_cards": [
                    {
                      "title": "card 1",
                      "question": "Capital of Brazil",
                      "answer": "Brasilia",
                      "tags": ["america", "cities"]
                    }
                  ],
                  "scq_cards": [
                    {
                      "title": "card 5",
                      "question":"What is the root of this equation: 3x + 2 = 14",
                      "options": ["5", "9", "3", "4"],
                      "correctOption": 3,
                      "tags": ["equations"]
                    }
                  ],
                  "mcq_cards": [
                    {
                      "title": "card 9",
                      "question": "Select all correct statements about this equation: ax + by = c",
                      "options": [
                        "It's a quadratic equation",
                        "It's a linear equation",
                        "It describes a line",
                        "It describes a curve"
                      ],
                      "correctOptions": [1, 2],
                      "tags": ["geometry", "equations"]
                    }
                  ]
                }
                """;
        var testMapper = new ObjectMapper();
        List<UserDto> userDTOs = testMapper.readValue(userJson, new TypeReference<>() {});
        JsonNode jsonNode = testMapper.readTree(flashcardJson);
        //noinspection unchecked
        when(objectMapper.readValue(any(File.class), any(TypeReference.class))).thenReturn(userDTOs);
        when(objectMapper.readTree(any(File.class))).thenReturn(jsonNode);

        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);
        when(mongoDatabase.listCollectionNames()).thenReturn(mongoIterable);
        when(mongoIterable.into(any())).thenReturn(new ArrayList<>(List.of("example", "user", "category")));
        when(mongoDatabase.getCollection("example")).thenReturn(mongoCollection);
        when(mongoDatabase.getCollection("user")).thenReturn(mongoCollection);
        when(mongoDatabase.getCollection("category")).thenReturn(mongoCollection);
        when(mongoCollection.countDocuments()).thenReturn(0L);

        when(userMapper.toDocument(any())).thenReturn(new User());

        dataInitializer.init();

        verify(userMapper, times(1)).toDocument(any());
        verify(mongoTemplate, times(1)).insertAll(any());
        // TODO replace next line by the following as soon as 5 times load is removed
        verify(mongoTemplate, times(15)).insert(any(), eq("example"));
        //verify(mongoTemplate, times(3)).insert(any(), eq("example"));
    }
}
