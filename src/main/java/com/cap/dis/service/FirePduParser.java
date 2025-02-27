package com.cap.dis.service;

import com.cap.dis.model.FireEventRecord;
import com.cap.dis.model.PduMessage;
import com.cap.dis.repository.FireEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FirePduParser implements PduParser {
    private static final Logger log = LoggerFactory.getLogger(FirePduParser.class);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FireEventRepository repository;

    @Override
    @Transactional
    public void parseAndStore(String jsonMessage) {
        try {
            PduMessage pduMessage = objectMapper.readValue(jsonMessage, PduMessage.class);
            if (pduMessage.getFiringEntityId() == null || pduMessage.getTargetEntityId() == null || pduMessage.getMunitionId() == null) {
                log.error("Missing required fields in FirePdu: {}", jsonMessage);
                return;
            }
            FireEventRecord record = new FireEventRecord();
            record.setFiringSite(pduMessage.getFiringEntityId().getSite());
            record.setFiringApplication(pduMessage.getFiringEntityId().getApplication());
            record.setFiringEntity(pduMessage.getFiringEntityId().getEntity());
            record.setTargetSite(pduMessage.getTargetEntityId().getSite());
            record.setTargetApplication(pduMessage.getTargetEntityId().getApplication());
            record.setTargetEntity(pduMessage.getTargetEntityId().getEntity());
            record.setMunitionSite(pduMessage.getMunitionId().getSite());
            record.setMunitionApplication(pduMessage.getMunitionId().getApplication());
            record.setMunitionEntity(pduMessage.getMunitionId().getEntity());
            record.setTimestamp(pduMessage.getTimestamp());
            repository.save(record);
            log.info("Stored FireEventRecord: {}", record);
        } catch (Exception e) {
            log.error("Failed to parse and store FirePdu: {}", jsonMessage, e);
        }
    }
}