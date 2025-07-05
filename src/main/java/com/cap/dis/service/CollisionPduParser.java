package com.cap.dis.service;

import com.cap.dis.model.CollisionRecord;
import com.cap.dis.model.PduMessage;
import com.cap.dis.repository.CollisionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CollisionPduParser implements PduParser {
    private static final Logger log = LoggerFactory.getLogger(CollisionPduParser.class);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CollisionRepository repository;

    @Override
    @Transactional
    public void parseAndStore(String jsonMessage) {
        try {
            PduMessage pduMessage = objectMapper.readValue(jsonMessage, PduMessage.class);
            if (pduMessage.getIssuingEntityId() == null || pduMessage.getCollidingEntityId() == null) {
                log.error("Missing required fields in CollisionPdu: {}", jsonMessage);
                return;
            }
            CollisionRecord record = new CollisionRecord();
            record.setIssuingSite(pduMessage.getIssuingEntityId().getSite());
            record.setIssuingApplication(pduMessage.getIssuingEntityId().getApplication());
            record.setIssuingEntity(pduMessage.getIssuingEntityId().getEntity());
            record.setCollidingSite(pduMessage.getCollidingEntityId().getSite());
            record.setCollidingApplication(pduMessage.getCollidingEntityId().getApplication());
            record.setCollidingEntity(pduMessage.getCollidingEntityId().getEntity());
            record.setTimestamp(pduMessage.getTimestamp());
            log.info("Attempting to store CollisionRecord with timestamp: {}", record.getTimestamp());
            repository.save(record);
            log.info("Stored CollisionRecord: {}", record);
        } catch (Exception e) {
            log.error("Failed to parse and store CollisionPdu: {}", jsonMessage, e);
        }
    }
}