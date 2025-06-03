package com.cap.dis.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import static org.mockito.Mockito.*; // If we were to mock logger

@ExtendWith(MockitoExtension.class)
class DefaultPduParserTest {

    @InjectMocks
    private DefaultPduParser defaultPduParser;

    // private static final Logger log = LoggerFactory.getLogger(DefaultPduParser.class); // For logger mocking

    @Test
    void parseAndStore_shouldExecuteWithoutError() {
        // Arrange
        String testJsonMessage = "{\"type\":\"UnknownPdu\", \"data\":\"some_data\"}";

        // Act & Assert
        // Primarily ensures no exceptions are thrown.
        // Verifying log output typically requires more setup (e.g., capturing appender).
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
            defaultPduParser.parseAndStore(testJsonMessage); // 
        });

        // If you had a mockable logger injected or a test appender:
        // verify(mockedLog).warn(eq("Received unhandled PDU type: {}"), eq(testJsonMessage));
    }
}