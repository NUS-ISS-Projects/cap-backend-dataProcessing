package com.cap.dis.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PduProcessingService {

    private static final Logger log = LoggerFactory.getLogger(PduProcessingService.class);

    private final ObjectMapper objectMapper;
    private final PduParserFactory parserFactory;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${spring.kafka.consumer.group-id}", concurrency = "3")
    public void consume(String message) {
        log.info("Received message from Kafka: {}", message);
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            
            // Check if this is an error message from the data ingestion service
            if (jsonNode.has("error")) {
                log.warn("Received error message from data ingestion service: {}", jsonNode.get("error").asText());
                return;
            }
            
            // Now safely get the type field
            JsonNode typeNode = jsonNode.get("type");
            if (typeNode == null) {
                log.error("Missing PDU type in message: {}", message);
                return;
            }
            
            String pduType = typeNode.asText();
            if (pduType.isEmpty()) {
                log.error("Empty PDU type in message: {}", message);
                return;
            }
            
            log.debug("Extracted PDU type: '{}'", pduType);  // Debug log for type
            long timestamp = jsonNode.has("timestamp") ? jsonNode.get("timestamp").asLong() : System.currentTimeMillis();
            log.debug("Processing PDU type: {} with timestamp: {}", pduType, timestamp);
            PduParser parser = parserFactory.getParser(pduType);
            parser.parseAndStore(message);
            log.info("Processed PDU type: {} with timestamp: {}", pduType, timestamp);
        } catch (Exception e) {
            log.error("Error processing message: {} - Exception: {}", message, e.getMessage(), e);
        }
    }
}