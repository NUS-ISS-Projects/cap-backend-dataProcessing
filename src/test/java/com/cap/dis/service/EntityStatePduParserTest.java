package com.cap.dis.service;

import com.cap.dis.model.EntityId;
import com.cap.dis.model.EntityStateRecord;
import com.cap.dis.model.Location;
import com.cap.dis.model.PduMessage;
import com.cap.dis.repository.EntityStateRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntityStatePduParserTest {

    @Mock
    private EntityStateRepository repository; // 

    @Spy // Using a real ObjectMapper instance, can be @Mock if specific behavior is needed
    private ObjectMapper objectMapper = new ObjectMapper(); // 

    @InjectMocks
    private EntityStatePduParser entityStatePduParser;

    @Test
    void parseAndStore_validEntityStatePdu_shouldSaveRecord() throws Exception {
        // Arrange
        String validJson = "{\"type\":\"EntityStatePdu\",\"entityId\":{\"site\":1,\"application\":2,\"entity\":3},\"location\":{\"x\":10.0,\"y\":20.0,\"z\":30.0},\"timestamp\":1234567890}";
        PduMessage expectedPduMessage = objectMapper.readValue(validJson, PduMessage.class);

        // Act
        entityStatePduParser.parseAndStore(validJson); // 

        // Assert
        ArgumentCaptor<EntityStateRecord> recordCaptor = ArgumentCaptor.forClass(EntityStateRecord.class);
        verify(repository, times(1)).save(recordCaptor.capture()); // 

        EntityStateRecord savedRecord = recordCaptor.getValue();
        assertNotNull(savedRecord);
        assertEquals(expectedPduMessage.getEntityId().getSite(), savedRecord.getSite()); // 
        assertEquals(expectedPduMessage.getEntityId().getApplication(), savedRecord.getApplication());
        assertEquals(expectedPduMessage.getEntityId().getEntity(), savedRecord.getEntity());
        assertEquals(expectedPduMessage.getLocation().getX(), savedRecord.getLocationX());
        assertEquals(expectedPduMessage.getLocation().getY(), savedRecord.getLocationY());
        assertEquals(expectedPduMessage.getLocation().getZ(), savedRecord.getLocationZ());
        assertEquals(expectedPduMessage.getTimestamp(), savedRecord.getTimestamp());
    }

    @Test
    void parseAndStore_missingEntityId_shouldNotSaveAndLogError() { // 
        // Arrange
        String jsonMissingEntityId = "{\"type\":\"EntityStatePdu\",\"location\":{\"x\":10.0,\"y\":20.0,\"z\":30.0},\"timestamp\":1234567890}";

        // Act
        entityStatePduParser.parseAndStore(jsonMissingEntityId);

        // Assert
        verify(repository, never()).save(any(EntityStateRecord.class));
        // Add log verification if needed
    }

    @Test
    void parseAndStore_missingLocation_shouldNotSaveAndLogError() { // 
        // Arrange
        String jsonMissingLocation = "{\"type\":\"EntityStatePdu\",\"entityId\":{\"site\":1,\"application\":2,\"entity\":3},\"timestamp\":1234567890}";

        // Act
        entityStatePduParser.parseAndStore(jsonMissingLocation);

        // Assert
        verify(repository, never()).save(any(EntityStateRecord.class));
    }

    @Test
    void parseAndStore_objectMapperException_shouldNotSaveAndLogError() throws Exception {
        // Arrange
        String malformedJson = "{\"type\":\"EntityStatePdu\",, \"entityId\":...}"; // Malformed
        // Configure ObjectMapper mock to throw an exception if it were a mock
        // Since it's a @Spy with a real instance, this test relies on real parsing failure
        // If objectMapper were a @Mock:
        // when(objectMapper.readValue(anyString(), eq(PduMessage.class))).thenThrow(new JsonProcessingException("Test error") {});

        // Act
        entityStatePduParser.parseAndStore(malformedJson); // 

        // Assert
        verify(repository, never()).save(any(EntityStateRecord.class));
    }
}