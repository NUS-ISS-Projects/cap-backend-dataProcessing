package com.cap.dis.service;

import com.cap.dis.model.DataPduRecord;
import com.cap.dis.model.PduMessage;
import com.cap.dis.repository.DataPduRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataPduParser implements PduParser {
    private static final Logger log = LoggerFactory.getLogger(DataPduParser.class);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DataPduRepository repository;

    @Override
    @Transactional
    public void parseAndStore(String jsonMessage) {
        try {
            PduMessage pduMessage = objectMapper.readValue(jsonMessage, PduMessage.class);
            if (pduMessage.getOriginatingEntityId() == null || pduMessage.getReceivingEntityId() == null) {
                log.error("Missing required fields in DataPdu: {}", jsonMessage);
                return;
            }
            DataPduRecord record = new DataPduRecord();
            record.setOriginatingSite(pduMessage.getOriginatingEntityId().getSite());
            record.setOriginatingApplication(pduMessage.getOriginatingEntityId().getApplication());
            record.setOriginatingEntity(pduMessage.getOriginatingEntityId().getEntity());
            record.setReceivingSite(pduMessage.getReceivingEntityId().getSite());
            record.setReceivingApplication(pduMessage.getReceivingEntityId().getApplication());
            record.setReceivingEntity(pduMessage.getReceivingEntityId().getEntity());
            record.setTimestamp(pduMessage.getTimestamp());
            log.info("Attempting to store DataPduRecord with timestamp: {}", record.getTimestamp());
            repository.save(record);
            log.info("Stored DataPduRecord: {}", record);
        } catch (Exception e) {
            log.error("Failed to parse and store DataPdu: {}", jsonMessage, e);
        }
    }
}