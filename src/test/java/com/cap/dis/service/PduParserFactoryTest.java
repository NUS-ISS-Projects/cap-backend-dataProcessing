package com.cap.dis.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
class PduParserFactoryTest {

    @Mock
    private EntityStatePduParser entityStatePduParser; // 

    @Mock
    private FirePduParser firePduParser; // 

    @Mock
    private DefaultPduParser defaultPduParser; // 

    @InjectMocks
    private PduParserFactory pduParserFactory;

    @BeforeEach
    void setUp() {
        // Manually call init because @PostConstruct is not invoked in plain unit tests
        // without Spring context. Alternatively, make init() public or package-private.
        // Or, inject mocks via constructor if PduParserFactory's constructor allows it.
        // For this example, we'll assume @Autowired fields are set and init() can be called.
        // If init is private, use ReflectionTestUtils or make it package-private.
        // For simplicity, let's assume init is called or its logic is simple enough.
        // The @InjectMocks should handle setting the mocked parsers.
        // We then need to explicitly call init if it's not run automatically.
        pduParserFactory.init(); // 
    }

    @Test
    void getParser_shouldReturnEntityStatePduParserForEntityStatePduType() {
        // Act
        PduParser parser = pduParserFactory.getParser("EntityStatePdu"); // 

        // Assert
        assertNotNull(parser);
        assertSame(entityStatePduParser, parser, "Should return the mocked EntityStatePduParser");
    }

    @Test
    void getParser_shouldReturnFirePduParserForFirePduType() {
        // Act
        PduParser parser = pduParserFactory.getParser("FirePdu"); // 

        // Assert
        assertNotNull(parser);
        assertSame(firePduParser, parser, "Should return the mocked FirePduParser");
    }

    @Test
    void getParser_shouldReturnDefaultPduParserForUnknownType() {
        // Act
        PduParser parser = pduParserFactory.getParser("UnknownPduType"); // 

        // Assert
        assertNotNull(parser);
        assertSame(defaultPduParser, parser, "Should return the mocked DefaultPduParser for unknown types");
    }

    @Test
    void getParser_shouldReturnDefaultPduParserForNullType() {
        // Act
        PduParser parser = pduParserFactory.getParser(null);

        // Assert
        assertNotNull(parser);
        assertSame(defaultPduParser, parser, "Should return the mocked DefaultPduParser for null type");
    }
}