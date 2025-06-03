package com.cap.dis.service;

import com.cap.dis.model.FireEventRecord;
import com.cap.dis.model.PduMessage;
import com.cap.dis.repository.FireEventRepository;
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
class FirePduParserTest {

    @Mock
    private FireEventRepository repository; // 

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper(); // 

    @InjectMocks
    private FirePduParser firePduParser;

    @Test
    void parseAndStore_validFirePdu_shouldSaveRecord() throws Exception {
        // Arrange
        String validJson = "{\"type\":\"FirePdu\",\"firingEntityId\":{\"site\":1,\"application\":10,\"entity\":100},\"targetEntityId\":{\"site\":2,\"application\":20,\"entity\":200},\"munitionId\":{\"site\":3,\"application\":30,\"entity\":300},\"timestamp\":1234500000}";
        PduMessage expectedPduMessage = objectMapper.readValue(validJson, PduMessage.class);

        // Act
        firePduParser.parseAndStore(validJson); // 

        // Assert
        ArgumentCaptor<FireEventRecord> recordCaptor = ArgumentCaptor.forClass(FireEventRecord.class);
        verify(repository, times(1)).save(recordCaptor.capture()); // 

        FireEventRecord savedRecord = recordCaptor.getValue();
        assertNotNull(savedRecord);
        assertEquals(expectedPduMessage.getFiringEntityId().getSite(), savedRecord.getFiringSite()); // 
        assertEquals(expectedPduMessage.getTargetEntityId().getApplication(), savedRecord.getTargetApplication());
        assertEquals(expectedPduMessage.getMunitionId().getEntity(), savedRecord.getMunitionEntity());
        assertEquals(expectedPduMessage.getTimestamp(), savedRecord.getTimestamp());
    }

    @Test
    void parseAndStore_missingFiringEntityId_shouldNotSave() { // 
        // Arrange
        String jsonMissingFiring = "{\"type\":\"FirePdu\",\"targetEntityId\":{\"site\":2,\"application\":20,\"entity\":200},\"munitionId\":{\"site\":3,\"application\":30,\"entity\":300},\"timestamp\":1234500000}";

        // Act
        firePduParser.parseAndStore(jsonMissingFiring);

        // Assert
        verify(repository, never()).save(any(FireEventRecord.class));
    }

    @Test
    void parseAndStore_objectMapperException_shouldNotSave() throws Exception {
        // Arrange
        String malformedJson = "{\"type\":\"FirePdu\",,, INVALID";
        // If objectMapper were a @Mock:
        // when(objectMapper.readValue(anyString(), eq(PduMessage.class))).thenThrow(new JsonProcessingException("Test error"){});

        // Act
        firePduParser.parseAndStore(malformedJson); // 

        // Assert
        verify(repository, never()).save(any(FireEventRecord.class));
    }
}