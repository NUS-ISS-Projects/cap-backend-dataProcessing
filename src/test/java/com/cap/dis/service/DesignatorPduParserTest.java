package com.cap.dis.service;

import com.cap.dis.model.DesignatorPduRecord;
import com.cap.dis.model.PduMessage;
import com.cap.dis.repository.DesignatorPduRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesignatorPduParserTest {

    @Mock
    private DesignatorPduRepository repository;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private DesignatorPduParser designatorPduParser;

    @Test
    void parseAndStore_validDesignatorPdu_shouldSaveRecord() throws Exception {
        // Arrange
        String validJson = "{\"type\":\"DesignatorPdu\",\"designatingEntityId\":{\"site\":1,\"application\":2,\"entity\":3},\"designatedEntityId\":{\"site\":4,\"application\":5,\"entity\":6},\"timestamp\":1234567890}";
        PduMessage expectedPduMessage = objectMapper.readValue(validJson, PduMessage.class);

        // Act
        designatorPduParser.parseAndStore(validJson);

        // Assert
        ArgumentCaptor<DesignatorPduRecord> recordCaptor = ArgumentCaptor.forClass(DesignatorPduRecord.class);
        verify(repository, times(1)).save(recordCaptor.capture());

        DesignatorPduRecord savedRecord = recordCaptor.getValue();
        assertNotNull(savedRecord);
        assertEquals(expectedPduMessage.getDesignatingEntityId().getSite(), savedRecord.getDesignatingSite());
        assertEquals(expectedPduMessage.getDesignatingEntityId().getApplication(), savedRecord.getDesignatingApplication());
        assertEquals(expectedPduMessage.getDesignatingEntityId().getEntity(), savedRecord.getDesignatingEntity());
        assertEquals(expectedPduMessage.getDesignatedEntityId().getSite(), savedRecord.getDesignatedSite());
        assertEquals(expectedPduMessage.getDesignatedEntityId().getApplication(), savedRecord.getDesignatedApplication());
        assertEquals(expectedPduMessage.getDesignatedEntityId().getEntity(), savedRecord.getDesignatedEntity());
        assertEquals(expectedPduMessage.getTimestamp(), savedRecord.getTimestamp());
    }

    @Test
    void parseAndStore_missingDesignatingEntityId_shouldNotSave() {
        // Arrange
        String jsonMissingDesignating = "{\"type\":\"DesignatorPdu\",\"designatedEntityId\":{\"site\":4,\"application\":5,\"entity\":6},\"timestamp\":1234567890}";

        // Act
        designatorPduParser.parseAndStore(jsonMissingDesignating);

        // Assert
        verify(repository, never()).save(any(DesignatorPduRecord.class));
    }

    @Test
    void parseAndStore_missingDesignatedEntityId_shouldNotSave() {
        // Arrange
        String jsonMissingDesignated = "{\"type\":\"DesignatorPdu\",\"designatingEntityId\":{\"site\":1,\"application\":2,\"entity\":3},\"timestamp\":1234567890}";

        // Act
        designatorPduParser.parseAndStore(jsonMissingDesignated);

        // Assert
        verify(repository, never()).save(any(DesignatorPduRecord.class));
    }

    @Test
    void parseAndStore_objectMapperException_shouldNotSave() {
        // Arrange
        String malformedJson = "{\"type\":\"DesignatorPdu\",,, INVALID";

        // Act
        designatorPduParser.parseAndStore(malformedJson);

        // Assert
        verify(repository, never()).save(any(DesignatorPduRecord.class));
    }
}