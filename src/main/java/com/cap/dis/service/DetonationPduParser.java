package com.cap.dis.service;

import com.cap.dis.model.DetonationRecord;
import com.cap.dis.model.PduMessage;
import com.cap.dis.repository.DetonationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DetonationPduParser implements PduParser {
    private static final Logger log = LoggerFactory.getLogger(DetonationPduParser.class);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DetonationRepository repository;

    @Override
    @Transactional
    public void parseAndStore(String jsonMessage) {
        try {
            PduMessage pduMessage = objectMapper.readValue(jsonMessage, PduMessage.class);
            if (pduMessage.getFiringEntityId() == null || pduMessage.getTargetEntityId() == null) {
                log.error("Missing required fields in DetonationPdu: {}", jsonMessage);
                return;
            }
            DetonationRecord record = new DetonationRecord();
            record.setFiringSite(pduMessage.getFiringEntityId().getSite());
            record.setFiringApplication(pduMessage.getFiringEntityId().getApplication());
            record.setFiringEntity(pduMessage.getFiringEntityId().getEntity());
            record.setTargetSite(pduMessage.getTargetEntityId().getSite());
            record.setTargetApplication(pduMessage.getTargetEntityId().getApplication());
            record.setTargetEntity(pduMessage.getTargetEntityId().getEntity());
            
            // If location is available, use it
            if (pduMessage.getLocation() != null) {
                record.setLocationX(pduMessage.getLocation().getX());
                record.setLocationY(pduMessage.getLocation().getY());
                record.setLocationZ(pduMessage.getLocation().getZ());
            }
            
            record.setTimestamp(pduMessage.getTimestamp());
            log.info("Attempting to store DetonationRecord with timestamp: {}", record.getTimestamp());
            repository.save(record);
            log.info("Stored DetonationRecord: {}", record);
        } catch (Exception e) {
            log.error("Failed to parse and store DetonationPdu: {}", jsonMessage, e);
        }
    }
}