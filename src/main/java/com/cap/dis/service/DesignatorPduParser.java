package com.cap.dis.service;

import com.cap.dis.model.DesignatorPduRecord;
import com.cap.dis.model.PduMessage;
import com.cap.dis.repository.DesignatorPduRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DesignatorPduParser implements PduParser {
    private static final Logger log = LoggerFactory.getLogger(DesignatorPduParser.class);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DesignatorPduRepository repository;

    @Override
    @Transactional
    public void parseAndStore(String jsonMessage) {
        try {
            PduMessage pduMessage = objectMapper.readValue(jsonMessage, PduMessage.class);
            if (pduMessage.getDesignatingEntityId() == null || pduMessage.getDesignatedEntityId() == null) {
                log.error("Missing required fields in DesignatorPdu: {}", jsonMessage);
                return;
            }
            DesignatorPduRecord record = new DesignatorPduRecord();
            record.setDesignatingSite(pduMessage.getDesignatingEntityId().getSite());
            record.setDesignatingApplication(pduMessage.getDesignatingEntityId().getApplication());
            record.setDesignatingEntity(pduMessage.getDesignatingEntityId().getEntity());
            record.setDesignatedSite(pduMessage.getDesignatedEntityId().getSite());
            record.setDesignatedApplication(pduMessage.getDesignatedEntityId().getApplication());
            record.setDesignatedEntity(pduMessage.getDesignatedEntityId().getEntity());
            record.setTimestamp(pduMessage.getTimestamp());
            log.info("Attempting to store DesignatorPduRecord with timestamp: {}", record.getTimestamp());
            repository.save(record);
            log.info("Stored DesignatorPduRecord: {}", record);
        } catch (Exception e) {
            log.error("Failed to parse and store DesignatorPdu: {}", jsonMessage, e);
        }
    }
}