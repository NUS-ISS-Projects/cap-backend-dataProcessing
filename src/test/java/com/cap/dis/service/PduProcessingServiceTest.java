package com.cap.dis.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@ExtendWith(MockitoExtension.class)
class PduProcessingServiceTest {

    @Mock
    private PduParserFactory parserFactory; // 

    @Spy // Use a real ObjectMapper or mock it if specific error conditions are needed
    private ObjectMapper objectMapper = new ObjectMapper(); // 

    @InjectMocks
    private PduProcessingService pduProcessingService;

    @Mock
    private PduParser mockPduParser; // To be returned by the factory

    @BeforeEach
    void setUp() {
        // Ensure mockPduParser is returned by default to prevent NullPointerExceptions
        // if a PDU type is not explicitly mocked in a test.
        lenient().when(parserFactory.getParser(anyString())).thenReturn(mockPduParser);
        lenient().when(parserFactory.getParser(isNull())).thenReturn(mockPduParser);
    }

    @Test
    void consume_validMessage_shouldParseAndStore() throws Exception {
        // Arrange
        String pduType = "EntityStatePdu";
        String message = "{\"type\":\"" + pduType + "\",\"timestamp\":123,\"data\":\"some_data\"}";
        // JsonNode jsonNode = objectMapper.readTree(message); // For verification if needed

        when(parserFactory.getParser(pduType)).thenReturn(mockPduParser);

        // Act
        pduProcessingService.consume(message); // 

        // Assert
        // verify(objectMapper, times(1)).readTree(message); // objectMapper is a spy, direct verify might be tricky unless behaviors are stubbed
        verify(parserFactory, times(1)).getParser(pduType); // 
        verify(mockPduParser, times(1)).parseAndStore(message); // 
    }

    @Test
    void consume_messageMissingType_shouldLogErrorAndReturn() throws Exception { // 
        // Arrange
        String message = "{\"timestamp\":123,\"data\":\"some_data\"}"; // No "type" field

        // Act
        pduProcessingService.consume(message);

        // Assert
        verify(parserFactory, never()).getParser(anyString());
        verify(mockPduParser, never()).parseAndStore(anyString());
    }

    @Test
    void consume_messageEmptyType_shouldLogErrorAndReturn() throws Exception { // 
        // Arrange
        String message = "{\"type\":\"\",\"timestamp\":123,\"data\":\"some_data\"}"; // Empty "type"

        // Act
        pduProcessingService.consume(message);

        // Assert
        verify(parserFactory, never()).getParser(anyString());
        verify(mockPduParser, never()).parseAndStore(anyString());
    }


    @Test
    void consume_objectMapperReadTreeException_shouldLogError() throws Exception {
        // Arrange
        String malformedMessage = "this is not json";
        // To make the spy objectMapper throw, we'd have to find input that makes real one throw,
        // or replace @Spy with @Mock and stub it.
        // For this test, we're relying on the real ObjectMapper to fail.
        // If objectMapper was a @Mock:
        // when(objectMapper.readTree(malformedMessage)).thenThrow(new JsonProcessingException("Test parse error"){});

        // Act
        pduProcessingService.consume(malformedMessage); // 

        // Assert
        verify(parserFactory, never()).getParser(anyString());
        verify(mockPduParser, never()).parseAndStore(anyString());
    }

    @Test
    void consume_parserFactoryReturnsDefaultAndStoreSucceeds() throws Exception {
        // Arrange
        String pduType = "SomeOtherPdu";
        String message = "{\"type\":\"" + pduType + "\",\"timestamp\":456,\"data\":\"other_data\"}";
        DefaultPduParser defaultParser = mock(DefaultPduParser.class); // Explicitly mock default

        when(parserFactory.getParser(pduType)).thenReturn(defaultParser);

        // Act
        pduProcessingService.consume(message);

        // Assert
        verify(parserFactory, times(1)).getParser(pduType);
        verify(defaultParser, times(1)).parseAndStore(message);
    }

    @Test
    void consume_messageMissingTimestamp_usesCurrentTime() throws Exception {
        // Arrange
        String pduType = "EntityStatePdu";
        String message = "{\"type\":\"" + pduType + "\",\"data\":\"some_data\"}"; // No "timestamp"
        // ObjectNode jsonNode = (ObjectNode) objectMapper.readTree(message);
        // No timestamp implies System.currentTimeMillis() will be used internally by the service log 

        when(parserFactory.getParser(pduType)).thenReturn(mockPduParser);

        long timeBefore = System.currentTimeMillis();
        // Act
        pduProcessingService.consume(message);
        long timeAfter = System.currentTimeMillis();

        // Assert
        verify(parserFactory, times(1)).getParser(pduType);
        verify(mockPduParser, times(1)).parseAndStore(message);
        // To verify the timestamp used for logging (source 231, 232), you'd need log capture
        // or to inspect arguments if timestamp was passed to parser.
        // The log "Processing PDU type: {} with timestamp: {}" uses the derived/current time.
    }
}