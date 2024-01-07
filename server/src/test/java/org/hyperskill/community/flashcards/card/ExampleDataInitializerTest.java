package org.hyperskill.community.flashcards.card;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.Document;
import org.hyperskill.community.flashcards.registration.User;
import org.hyperskill.community.flashcards.registration.UserMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@MockitoSettings
class ExampleDataInitializerTest {
    @Mock
    MongoTemplate mongoTemplate;

    @Mock
    MongoDatabase mongoDatabase;

    @Mock
    MongoIterable<String> mongoIterable;

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
    void whenCollectionsEmpty_insertData() {
        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);
        when(mongoDatabase.listCollectionNames()).thenReturn(mongoIterable);
        when(mongoIterable.into(any())).thenReturn(new ArrayList<>(List.of("example", "user", "category")));
        when(mongoDatabase.getCollection("example")).thenReturn(mongoCollection);
        when(mongoDatabase.getCollection("user")).thenReturn(mongoCollection);
        when(mongoDatabase.getCollection("category")).thenReturn(mongoCollection);
        when(mongoCollection.countDocuments()).thenReturn(0L);
        when(userMapper.toDocument(any())).thenReturn(new User());

        dataInitializer.init();

        verify(userMapper, times(2)).toDocument(any());
        verify(mongoTemplate, times(1)).insertAll(any());
        verify(mongoTemplate, times(3)).insert(any(), eq("example"));
    }
}
