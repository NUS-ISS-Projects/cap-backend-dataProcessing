package com.cap.dis.service;

import com.cap.dis.model.EntityStateRecord;
import com.cap.dis.model.PduMessage;
import com.cap.dis.repository.EntityStateRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PduProcessingService {

    private static final Logger log = LoggerFactory.getLogger(PduProcessingService.class);

    private final EntityStateRepository repository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${spring.kafka.consumer.group-id}", concurrency = "3")
    @Transactional
    public void consume(String message) {
        log.info("Received message from Kafka: {}", message);
        try {
            // Deserialize the JSON message into a PduMessage object
            PduMessage pduMessage = objectMapper.readValue(message, PduMessage.class);
            if ("EntityStatePdu".equals(pduMessage.getType())) {
                storePduData(pduMessage);
            } else {
                log.warn("Received unknown PDU type: {}", pduMessage.getType());
            }
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }

    private void storePduData(PduMessage pduMessage) {
        try {
            EntityStateRecord record = new EntityStateRecord();
            record.setSite(pduMessage.getEntityId().getSite());
            record.setApplication(pduMessage.getEntityId().getApplication());
            record.setEntity(pduMessage.getEntityId().getEntity());
            record.setLocationX(pduMessage.getLocation().getX());
            record.setLocationY(pduMessage.getLocation().getY());
            record.setLocationZ(pduMessage.getLocation().getZ());
            record.setTimestamp(pduMessage.getTimestamp());
            repository.save(record);
            log.info("Saved EntityStateRecord: {}", record);
        } catch (Exception e) {
            log.error("Failed to store PDU record: {}", e.getMessage());
        }
    }
}
