package com.cap.dis.service;

import com.cap.dis.model.SetDataPduRecord;
import com.cap.dis.model.PduMessage;
import com.cap.dis.repository.SetDataPduRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetDataPduParser implements PduParser {
    private static final Logger log = LoggerFactory.getLogger(SetDataPduParser.class);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SetDataPduRepository repository;

    @Override
    @Transactional
    public void parseAndStore(String jsonMessage) {
        try {
            PduMessage pduMessage = objectMapper.readValue(jsonMessage, PduMessage.class);
            if (pduMessage.getOriginatingEntityId() == null) {
                log.error("Missing required fields in SetDataPdu: {}", jsonMessage);
                return;
            }
            SetDataPduRecord record = new SetDataPduRecord();
            record.setOriginatingSite(pduMessage.getOriginatingEntityId().getSite());
            record.setOriginatingApplication(pduMessage.getOriginatingEntityId().getApplication());
            record.setOriginatingEntity(pduMessage.getOriginatingEntityId().getEntity());
            record.setTimestamp(pduMessage.getTimestamp());
            log.info("Attempting to store SetDataPduRecord with timestamp: {}", record.getTimestamp());
            repository.save(record);
            log.info("Stored SetDataPduRecord: {}", record);
        } catch (Exception e) {
            log.error("Failed to parse and store SetDataPdu: {}", jsonMessage, e);
        }
    }
}