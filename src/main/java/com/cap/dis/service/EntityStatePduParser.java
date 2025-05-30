package com.cap.dis.service;

import com.cap.dis.model.EntityStateRecord;
import com.cap.dis.model.PduMessage;
import com.cap.dis.repository.EntityStateRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EntityStatePduParser implements PduParser {
    private static final Logger log = LoggerFactory.getLogger(EntityStatePduParser.class);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EntityStateRepository repository;

    @Override
    @Transactional
    public void parseAndStore(String jsonMessage) {
        try {
            PduMessage pduMessage = objectMapper.readValue(jsonMessage, PduMessage.class);
            if (pduMessage.getEntityId() == null || pduMessage.getLocation() == null) {
                log.error("Missing required fields in EntityStatePdu: {}", jsonMessage);
                return;
            }
            EntityStateRecord record = new EntityStateRecord();
            record.setSite(pduMessage.getEntityId().getSite());
            record.setApplication(pduMessage.getEntityId().getApplication());
            record.setEntity(pduMessage.getEntityId().getEntity());
            record.setLocationX(pduMessage.getLocation().getX());
            record.setLocationY(pduMessage.getLocation().getY());
            record.setLocationZ(pduMessage.getLocation().getZ());
            record.setTimestamp(pduMessage.getTimestamp());
            log.info("Attempting to store EntityStateRecord with timestamp: {}", record.getTimestamp()); // <<< ADD THIS LOG
            repository.save(record);
            log.info("Stored EntityStateRecord: {}", record);
        } catch (Exception e) {
            log.error("Failed to parse and store EntityStatePdu: {}", jsonMessage, e);
        }
    }
}